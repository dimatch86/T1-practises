#!/bin/bash

HELM_NAME=chart
CHART_FOLDER="/opt/app-chart"
MY_VALUES="/opt/my2.yaml"
RESOURCES_VALUES="/opt/resources.yaml"
touch "${RESOURCES_VALUES}"

#Задаем конфигурацию для kubectl
kubectl config set-cluster minikube --server=https://minikube:8443 --certificate-authority=/root/.kube/ca.crt
kubectl config set-credentials minikube --client-key=/root/.kube/client.key --client-certificate=/root/.kube/client.crt
kubectl config set-context minikube --cluster=minikube --user=minikube
kubectl config use-context minikube

helm_run() {
  local STANDID=$1
  local HELM_EXISTS

  HELM_EXISTS=$(helm ls | grep -c ${HELM_NAME})
  if [[ "${HELM_EXISTS}" == 0 ]]; then
    echo "### HELM: install  ====================================================="
    helm install "${HELM_NAME}" "${STANDID}" -f "${MY_VALUES}" -f "${RESOURCES_VALUES}"
  else
    echo "### HELM: upgrade  ====================================================="
    helm upgrade "${HELM_NAME}" "${STANDID}" -f "${MY_VALUES}" -f "${RESOURCES_VALUES}"
  fi
}

helm_run "${CHART_FOLDER}"