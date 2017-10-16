#!/bin/sh

# Script qui automatise les tests codegen
# sur les -codegen contenus dans deca/codegenax/valid/references/codegen
# et dans deca/codegenax/invalid/references/codegen

# Répertoire de test
TEST_DIR="$(dirname "$0")"/../deca/codegen

# Test des -codegen
echo "Test CODEGEN"
compteur=0

echo "valid"
VAL_DIR=$TEST_DIR/"valid"
# Références comparatives
REF_DIR=$VAL_DIR/references
# Création du répertoire de travail
RES_DIR=$VAL_DIR/res
mkdir $RES_DIR
for i in $REF_DIR/*
do
	if [ -f "$i" ]; then
	    nom="$(echo "$i" | rev | cut -d "/" -f1 | rev | cut -d "-" -f1)"
	    rm -f $RES_DIR/"$nom"-codegen
	    touch $RES_DIR/"$nom"-codegen
	    # Compilation du test
	    "$(dirname "$0")"/../../main/bin/decac $VAL_DIR/"$nom".deca
	    ima $VAL_DIR/"$nom".ass > $RES_DIR/"$nom"-codegen
	    # Suppression du fichier .ass généré
	    rm $VAL_DIR/"$nom".ass
	    DIFF=$(diff $RES_DIR/$nom-codegen $i)
	    # Inspiré de l'Ensiwiki pour tester la nullité d'une variable
	    if [ "${DIFF}"1 = 1 ]; then
		echo "$nom : Success"
	    else
		echo "$nom : Failure"
		echo "$DIFF"
		compteur=1
	    fi
	fi
    done

echo "invalid"
VAL_DIR=$TEST_DIR/"invalid"
# Références comparatives
REF_DIR=$VAL_DIR/references
# Création du répertoire de travail
RES_DIR=$VAL_DIR/res
mkdir $RES_DIR
for i in $REF_DIR/*
do
    	if [ -f "$i" ]; then
    	    nom="$(echo "$i" | rev | cut -d "/" -f1 | rev | cut -d "-" -f1)"
    	    rm -f $RES_DIR/"$nom"-codegen
    	    touch $RES_DIR/"$nom"-codegen
    	    # Compilation du test
    	    "$(dirname "$0")"/../../main/bin/decac $VAL_DIR/"$nom".deca
    	    ima $VAL_DIR/"$nom".ass > $RES_DIR/"$nom"-codegen
    	    # Suppression du fichier .ass généré
    	    rm $VAL_DIR/"$nom".ass
    	    DIFF=$(diff $RES_DIR/$nom-codegen $i)
    	    # Inspiré de l'Ensiwiki pour tester la nullité d'une variable
    	    if [ "${DIFF}"1 = 1 ]; then
    		echo "$nom : Success"
    	    else
    		echo "$nom : Failure"
    		echo "$DIFF"
    		compteur=1
    	    fi
    	fi
done


    # Destruction répertoire de travail
    if [ -d "$RES_DIR" ]; then
	rm -r $RES_DIR
    fi
    if [ $compteur -eq 0 ]; then
	exit 0
    else
	exit 1
    fi
done
