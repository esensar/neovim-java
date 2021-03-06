#!/bin/bash

# Deploy and sign a jar, source jar and javadoc jar to Sonatype's staging repo. After this it has to be released on https://oss.sonatype.org/#stagingRepositories
# It requires proper GPG signing configuration in local maven settings

# Also deploys to github
# Requires GH_TOKEN and github profile set up
key="$1"

case $key in
  -nv|--next-version)
  NEXT_VERSION="$2"
  shift # past argument
  shift # past value
  ;;
  *)    # unknown option
  echo "Pass next version with -nv or --next-version"
  exit 1
  ;;
esac

if [ -z "$NEXT_VERSION" ]
then
  echo "Missing next version"
  echo "Pass next version with -nv or --next-version"
  exit 1
fi

set -e

echo "Removing SNAPSHOT from version"
mvn versions:set -DremoveSnapshot
echo "Deploying to staging."
mvn clean source:jar javadoc:jar verify gpg:sign deploy -DskipTests -P release
# mvn deploy -DaltDeploymentRepository=github::default::https://maven.pkg.github.com/esensar/neovim-java -Dtoken=$GH_TOKEN -P github
echo "Deployed to staging. Visit https://oss.sonatype.org/#stagingRepositories to release."
echo "Changing version to $NEXT_VERSION-SNAPSHOT. Make sure to commit this"
mvn versions:set -DnewVersion=$NEXT_VERSION-SNAPSHOT
echo "Changed version to $NEXT_VERSION-SNAPSHOT. Make sure to commit this"
