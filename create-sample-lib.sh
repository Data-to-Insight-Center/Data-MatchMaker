#!/bin/sh

mvn dependency:copy-dependencies
mv target/dependency samples/lib
