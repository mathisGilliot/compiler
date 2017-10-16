# /bin/sh

PATH=./src/main/bin:"$PATH"

test_decac_b() {
  if [ "$1" = "" ]; then
  decac_b=$(decac -b)
else
  decac_b=$(decac -b "$1")
fi

#  if [ "$?" -ne 0 ]; then
#      echo "\033[31mERREUR\033[0m: decac -b a termine avec un status different de zero."
#      exit 1
#  fi

  if [ "$decac_b" = "" ]; then
      echo "\033[31mERREUR\033[0m: decac -b n'a produit aucune sortie"
      exit 1
  fi

  if echo "$decac_b" | grep -i -e "erreur" -e "error";
  then
      echo "\033[31mERREUR\033[0m: La sortie de decac -b contient erreur ou error"
      exit 1
  fi
  if echo "$decac_b" | grep -i "Options";
  then
    echo "\033[31mERREUR\033[0m: La sortie de decac -b est l'option display"
  else
  echo "\033[32mPas de probleme\033[0m detecte avec decac -b."
  fi
}


test_decac_p () {
    decac_p=$(decac -p "$1")
    if "$decac_p" 2>&1 | grep -q -i "$1"
    then
        echo  "\033[31mERREUR\033[0m: decac -p a determiné une erreur"
        #exit 1
    else
        echo  "\033[32mPas de probleme\033[0m detecte avec decac -p pour $1"
    fi
}

test_decac_p_v () {
    decac_p_v=$(decac -p -v "$1")
    if echo "$decac_p_v" | grep -q -i "option";
    then
        echo  "\033[31mERREUR\033[0m: decac -p -v a determiné une erreur"
        #exit 1
    else
        echo  "\033[32mPas de probleme\033[0m detecte avec decac -p -v pour $1"
    fi
}

test_decac_v () {
    decac_v=$(decac -v "$1")
    if "$decac_v" 2>&1 | grep -q -i "$1"
    then
        echo  "\033[31mERREUR\033[0m: decac -v a determiné une erreur"
        #exit 1
    else
        echo  "\033[32mPas de probleme\033[0m detecte avec decac -v pour $1"
    fi
}

test_decac_v_p () {
    decac_v_p=$(decac -v -p "$1")
    if "$decac_v" 2>&1 | grep -q -i "option"
    then
        echo  "\033[31mERREUR\033[0m: decac -v -p a determiné une erreur"
        #exit 1
    else
        echo  "\033[32mPas de probleme\033[0m detecte avec decac -v -p pour $1"
    fi
}

test_decac_d () {
    decac_d=$(decac -d "$1")
    if echo "$decac_d" | grep -i -q "erreur";
    then
        echo "\033[31mERREUR\033[0m: decac -d a determiné une erreur"
        #exit 1
    else
        echo "\033[32mPas de probleme\033[0m detecte avec decac -d pour $1"
    fi
}

test_decac_b
test_decac_b ./../../test/deca/syntax/valid/moitie.deca
test_decac_p ./../../test/deca/syntax/valid/moitie.deca
test_decac_v ./../../test/deca/syntax/valid/moitie.deca
test_decac_d ./../../test/deca/syntax/valid/moitie.deca
test_decac_p_v ./../../test/deca/syntax/valid/moitie.deca
test_decac_v_p ./../../test/deca/syntax/valid/moitie.deca
