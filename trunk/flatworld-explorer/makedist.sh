#!/bin/bash

NAME=flatworldexplorer-$1
mkdir $NAME

cp -r dist/lib $NAME
cp dist/*.jar $NAME
cp -r support/* $NAME

mkdir $NAME-source
cp -r dist/lib $NAME-source
cp -r src $NAME-source
cp -r support/*.txt $NAME-source
