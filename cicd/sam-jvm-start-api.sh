quarkus build --native --no-tests -Dquarkus.native.container-build=true
sam local start-api --template build/sam.native.yaml