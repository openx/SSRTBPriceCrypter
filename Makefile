JAR_VERSION := $(shell echo 'cat /*[local-name()="project"]/*[local-name()="version"]/text()' | xmllint --shell pom.xml | grep -v '/')

.PHONY: compile build test clean release package version

usage:
	@echo "USAGE:"
	@echo "   make command [options]"
	@echo
	@echo "COMMANDS:"
	@fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed 's/^/   /' | sed -e 's/\\$$/AA/' | sed -e 's/#//g' | column -t -s ":" | sort -k1

compile:
	mvn compile

package:
	mvn package

test:
	mvn verify

clean:
	mvn clean

release:
	mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true deploy

version:
	@echo $(JAR_VERSION)

format: ## Format code
	mvn com.coveo:fmt-maven-plugin:format

show-outdated-deps: ## Display which maven dependencies and plugins has newer stable versions
	mvn versions:display-dependency-updates
	mvn versions:display-plugin-updates

ci_publish:
	mvn deploy