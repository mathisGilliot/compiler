lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@header {
    import java.io.PrintStream;
    import java.lang.String;
}

@members {
}

// Deca lexer rules.

// Symbols
LT : '<';
GT : '>';
EQUALS : '=';
PLUS : '+' ;
MINUS :'-';
TIMES :'*';
PERCENT : '%';
SLASH :'/';
DOT : '.';
COMMA : ',';
OPARENT : '(';
CPARENT : ')';
OBRACE : '{';
CBRACE :'}';
EXCLAM : '!';
SEMI : ';';
EQEQ : '==';
NEQ : '!=';
GEQ : '>=';
LEQ : '<=';
AND : '&&';
OR : '||';

// Reserved words
ASM : 'asm';
CLASS : 'class';
EXTENDS : 'extends';
ELSE : 'else';
FALSE : 'false';
IF : 'if';
INSTANCEOF : 'instanceof';
NEW :'new';
NULL :'null';
READINT : 'readInt';
READFLOAT :'readFloat';
PRINT :'print';
PRINTLN : 'println';
PRINTLNX : 'printlnx';
PRINTX : 'printx';
PROTECTED : 'protected';
RETURN : 'return';
THIS : 'this';
TRUE : 'true';
WHILE : 'while';

// Comments
COM : '//' .*? '\n' { skip(); };
COMMENT : '/*' .*? '*/' { skip(); };

// Basic elements
fragment LETTER : 'a' .. 'z' | 'A' .. 'Z';
fragment DIGIT : '0' .. '9';

// Identificateur
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;

// Include
fragment FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"' {doInclude(getText());}; 

// Character
fragment STRING_CAR : ~ ('"' | '\\' | '\n');
STRING :  '"' (STRING_CAR | '\\"' | '\\\\' )*? '"' { String s = getText();
     												s = s.substring(1, s.length() - 1);
     												setText(s);
 												};
    												
MULTI_LINE_STRING : '"' (STRING_CAR | '\n' | '\\"' | '\\\\')*? '"' { String s = getText(); 
     											s = s.substring(1, s.length() - 1);
     											setText(s);
     											};
     											
// Integer
fragment POSITIVE_DIGIT : '1' .. '9';
INT : '0' | POSITIVE_DIGIT DIGIT*;

// Float
NUM : DIGIT+;
SIGN : ('+' | '-');
EXP : ('E' | 'e') SIGN? NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP) ('F' | 'f')?;
DIGITHEX : '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN? NUM ('F' | 'f')?;
FLOAT : FLOATDEC | FLOATHEX;

// Ignore spaces, tabs, newlines and whitespaces
WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {
              skip(); // avoid producing a token
          }
    ;
 
 //cf poly : '#' : caractere non reconnu
  
  
  
