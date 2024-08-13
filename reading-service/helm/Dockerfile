FROM  dtzar/helm-kubectl:latest

RUN mkdir -p /root/.kube

COPY /kube_config/ca.crt /root/.kube
COPY /kube_config/client.crt /root/.kube
COPY /kube_config/client.key /root/.kube

WORKDIR /opt
COPY /my2.yaml .
COPY /app-chart /opt/app-chart
COPY /app-sub-chart /opt/app-sub-chart
COPY /front-sub-chart /opt/front-sub-chart
COPY helm_run.sh /opt/helm_run.sh

RUN helm dependency update /opt/app-chart

CMD ["/opt/helm_run.sh"]
