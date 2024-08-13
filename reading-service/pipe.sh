#!/bin/bash

CDIR=$(pwd)
imageName=monit
repoName=dimatch86

#Проверка, что находимся в мастере и что совпадаем с удаленным репо
is_master() {
  # Получаем имя текущей ветки
  current_branch=$(git rev-parse --abbrev-ref HEAD)

  # Проверяем, является ли текущая ветка мастер-веткой или основной веткой
  if [ "$current_branch" != "master" ] && [ "$current_branch" != "main" ]; then
    echo "Для сборки образа нужно находиться в ветке master или main"
    exit 1
  fi

  # Проверяем, что текущий коммит совпадает с удаленным
  git fetch origin "$current_branch"
  local_commit=$(git rev-parse HEAD)
  remote_commit=$(git rev-parse origin/"$current_branch")

  if [ "$local_commit" != "$remote_commit" ]; then
    echo "Локальная ветка не на последнем коммите"
    exit 1
  fi

  echo "Находимся в ветке $current_branch и на последнем коммите"
}

#Проверка, что отсутствуют незафиксированные изменения
is_committed() {
  status_output=$(git status --porcelain | grep -cv '^??')
  if [ "${status_output}" != 0 ]; then
    echo "Есть незафиксированные изменения в проекте"
    exit 1
  fi
}

#Вычисляем хеш по файлам и пакетам
get_hash() {
  declare -a my_array=("/src" "/pom.xml" "/Dockerfile")
  total_hash=""

  for element in "${my_array[@]}"; do
    item="$1/$element"
    if [ ! -e "$item" ]; then
      continue
    fi
    hash=""
    if [ -d "$item" ]; then
      hash="$(find "$item" -type f -exec sha256sum {} + | awk '{ print $1 }')"
    else
      hash="$(sha256sum "$item" | awk '{ print $1 }')"
    fi
    total_hash+="${hash}"
  done

  echo -n "$total_hash" | sha256sum | head -c 8
}

# Проверяем наличие сборки в локальном репо
is_exist_local() {
  local image_name=$1
  local tag_name=$2

  result=$(docker images -q "$image_name":"$tag_name")
  if [ -z "$result" ]; then
    return 1
  fi
}

# Проверяем наличие сборки в удаленном репо
is_exist_remote() {
  local repository=$1
  local tag=$2
  local url="https://hub.docker.com/v2/repositories/${repository}/tags/${tag}"
  local http_code

  http_code=$(curl -s -o /dev/null -w "%{http_code}" "$url")
  if [ "$http_code" != 200 ]; then
    return 1
  fi
}

# Проверяем наличие такой сборки
check_tag() {
  local repo=$1
  local tag=$2
  if is_exist_local "$repo" "$tag"; then
    echo "Сборка найдена в локальном репо"
    exit 1
  fi
  if is_exist_remote "$repo" "$tag"; then
    echo "Сборка найдена в удаленном репо"
    exit 1
  fi
  echo "Сборка не найдена"
}

add_tag() {
  tag_name=$1
  git tag -a "${tag_name}" -m "Соответствует образу сборки с тегом - ${tag_name}"
  if [ $? -eq 1 ]; then
    echo "Тег ${tag_name} уже существует"
  fi
}

docker_push() {
  mvn dockerfile:push -Dproject.image.name="${repoName}/${imageName}" -Dtag.version="${hash}"
    if [ $? -eq 1 ]; then
      echo "Произошла ошибка во время сборки образа"
    fi
}


build_values() {
  PRJ_IMAGE=$1
  PRJ_TAG=$2

  if [ ! -f ./helm/my2.yaml ]; then
      echo "Создание нового файла my.yaml"
      echo "App_Sub_Chart:" > ./helm/my2.yaml
  fi

  IMAGE_VERSION=$PRJ_IMAGE yq e -i ".App_Sub_Chart.app.image.repository = strenv(IMAGE_VERSION)" ./helm/my2.yaml
  IMAGE_VERSION=$PRJ_TAG yq e -i ".App_Sub_Chart.app.image.tag = strenv(IMAGE_VERSION)" ./helm/my2.yaml
}

build_all() {
  #is_master && is_committed

  hash=$(get_hash "${CDIR}")
  check_tag "${repoName}/${imageName}" "${hash}"

  mvn dockerfile:build -Dproject.image.name="${repoName}/${imageName}" -Dtag.version="${hash}"
  status=$?

  if [ "${status}" -eq 1 ]; then
    hash="Error_${hash}"
    echo "Произошла ошибка во время сборки образа"
  fi
  docker_push

  add_tag "${hash}"
  build_values "${repoName}/${imageName}" "${hash}"
  cd ./helm || exit
  docker build -t helm .
}

build_all


