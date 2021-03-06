#!/bin/bash

function mvn_clean_verify() {
    mvn clean verify
}

set -ex
mvn_clean_verify
