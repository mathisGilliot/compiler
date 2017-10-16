#!/bin/sh

# Script qui automatise les tests codegen
# sur les -codegen contenus dans deca/codegenax/valid/references/codegen
# et dans deca/codegenax/invalid/references/codegen

REF_DIR=testARM
# Création du répertoire de travail



for i in $REF_DIR/*
do
	if [ -f "$i" ]; then
	    nom="$(echo "$i" | rev | cut -d "/" -f1 | rev | cut -d "-" -f1)"
	    # Compilation du test
	    ../src/main/bin/decac -ARM $REF_DIR/"$nom"
	fi
done

