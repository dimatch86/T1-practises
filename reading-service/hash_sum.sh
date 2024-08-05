#!/bin/bash

CDIR=$(pwd)
declare -a my_array=("/src" "/pom.xml" "/Dockerfile")
function get_hash() {

  total_hash=""

  for element in "${@:2}"; do
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

  echo -n "$total_hash" | sha256sum | awk '{ print $1 }'
}

get_dirs() {
  git diff --name-only HEAD^ HEAD | grep '/' | cut -d/ -f1 | sort | uniq
}

directories="$(get_dirs)"
for dir in $directories; do
  echo "${dir}"
  get_hash "${CDIR}/${dir}" "${my_array[@]}"
done


