quarkus build --native --no-tests -Dquarkus.native.container-build=true
sam deploy -t build/sam.native.yaml -g