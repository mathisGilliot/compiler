#!/bin/sh

# Script qui automatise les tests codegen
# sur les -codegen contenus dans deca/codegenax/valid/references/codegen
# et dans deca/codegenax/invalid/references/codegen

# Répertoire de test
TEST_DIR="$(dirname "$0")"/../deca/codegen

# Test des -codegen
echo "Test DECOMPILE"
compteur=0


echo "valid"
VAL_DIR=$TEST_DIR/"valid"
# Création du répertoire de travail
RES_DIR=$VAL_DIR/res
mkdir $RES_DIR
for i in $VAL_DIR/*
do
	if [ -f "$i" ]; then
	    nom="$(echo "$i" | rev | cut -d "/" -f1 | rev | cut -d "-" -f1)"
	    rm -f $RES_DIR/"$nom"-codegen
	    touch $RES_DIR/"$nom"-codegen
	    # Compilation du test
      decac_p=$(decac -p $VAL_DIR/"$nom")
      RES1="$decac_p"
      if [ "${RES1}"1 = 1 ]; then
    echo  "decac -p $nom :\033[31mERREUR\033[0m"
      else
    echo  "decac -p $nom :\033[32mSUCCESS\033[0m"
		compteur=1
	    fi
	fi
    done


echo "invalid"
VAL_DIR=$TEST_DIR/"invalid"
# Création du répertoire de travail
RES_DIR=$VAL_DIR/res
mkdir $RES_DIR
for i in $VAL_DIR/*
do
    	if [ -f "$i" ]; then
    	    nom="$(echo "$i" | rev | cut -d "/" -f1 | rev | cut -d "-" -f1)"
    	    rm -f $RES_DIR/"$nom"-codegen
    	    touch $RES_DIR/"$nom"-codegen
    	    # Compilation du test
          decac_p=$(decac -p $VAL_DIR/"$nom")
          RES1="$decac_p"
          if [ "${RES1}"1 = 1 ]; then
        echo  "decac -p $nom :\033[31mERREUR\033[0m"
          else
        echo  "decac -p $nom :\033[32mSUCCESS\033[0m"
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
