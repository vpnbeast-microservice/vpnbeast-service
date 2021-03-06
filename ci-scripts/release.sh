#!/bin/bash

function git_global_settings() {
    git config user.userName ${USERNAME}
    git config user.email ${EMAIL}
}

function git_commit_and_push() {
    git --no-pager diff
    git add --all
    git commit -am "[ci-skip] version ${RELEASE_VERSION}.RELEASE"
    git tag -a "${RELEASE_VERSION}.RELEASE" -m "v${RELEASE_VERSION} tagged"
    git status
    git push --follow-tags http://${USERNAME}:${GIT_ACCESS_TOKEN}@gitlab.com/${GROUP_NAME}/${PROJECT}.git HEAD:${BRANCH}
}

function docker_login() {
    docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}
}

function docker_build_image() {
    echo "building docker image with maven..."
    mvn install dockerfile:build
}

function docker_tag_image() {
    echo "tagging builded image with the version info..."
    docker tag ${DOCKER_USERNAME}/${PROJECT}:latest ${DOCKER_USERNAME}/${PROJECT}:${RELEASE_VERSION}
}

function docker_push_image() {
    echo "pushing with version info..."
    docker push ${DOCKER_USERNAME}/${PROJECT}:${RELEASE_VERSION}
    echo "pushing with latest tag..."
    docker push ${DOCKER_USERNAME}/${PROJECT}:latest
}

function mvn_get_version() {
    echo $(mvn -q \
        -Dexec.executable=echo \
        -Dexec.args='${project.version}' \
        --non-recursive \
        exec:exec | cut -d "=" -f 2)
}

function mvn_set_version() {
    mvn versions:set -DnewVersion=${RELEASE_VERSION}
    mvn versions:commit -B
}

function mvn_increment_minor_version() {
    local version_major=$(echo $1 | cut -d "." -f 1)
    local version_patch=$(echo $1 | cut -d "." -f 2)
    local version_minor=$(echo $1 | cut -d "." -f 3)
    version_minor=`expr ${version_minor} + 1`
    echo "${version_major}.${version_patch}.${version_minor}"
}

function mvn_get_name() {
    echo $(mvn -q \
        -Dexec.executable=echo \
        -Dexec.args='${project.name}' \
        --non-recursive \
        exec:exec | cut -d "=" -f 2)
}

function set_chart_version() {
    echo "previous docker image is ${DOCKER_USERNAME}/${PROJECT}:${CURRENT_VERSION}, changing it as ${DOCKER_USERNAME}/${PROJECT}:${RELEASE_VERSION}!"
    sed -i "s/${DOCKER_USERNAME}\/${PROJECT}:${CURRENT_VERSION}/${DOCKER_USERNAME}\/${PROJECT}:${RELEASE_VERSION}/g" charts/${PROJECT}/values.yaml
    sed -i "s/${CURRENT_VERSION}/${RELEASE_VERSION}/g" charts/${PROJECT}/Chart.yaml
}

set -ex
USERNAME=bilalcaliskan
GROUP_NAME="vpnbeast/backend"
EMAIL=bilalcaliskan@protonmail.com
PROJECT=$(mvn_get_name)
CURRENT_VERSION=$(mvn_get_version)
RELEASE_VERSION=$(mvn_increment_minor_version ${CURRENT_VERSION})
BRANCH=$1

docker_login
docker_build_image
docker_tag_image
docker_push_image
mvn_set_version
set_chart_version
git_global_settings
git_commit_and_push
