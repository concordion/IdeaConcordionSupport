package org.concordion.plugin.idea.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.concordion.plugin.idea.lang.psi.ConcordionTypes;
import com.intellij.psi.TokenType;

%%
%class ConcordionLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%{
  public ConcordionLexer(){
    this(null);
  }
%}

ALPHA=[:letter:]
DIGIT=[0-9]
WHITE_SPACE_CHAR=[\ \n\r\t\f]

IDENTIFIER={ALPHA} [:jletterdigit:]*
EXAMPLE_NAME=([:jletterdigit:]|\-)*

INTEGER_LITERAL=(0|([1-9]({DIGIT})*))

DOUBLE_LITERAL=({DOUBLE_LITERAL_1})|({DOUBLE_LITERAL_2})|({DOUBLE_LITERAL_3})
DOUBLE_LITERAL_1=({DIGIT})+"."({DIGIT})*({EXPONENT_PART})?
DOUBLE_LITERAL_2="."({DIGIT})+({EXPONENT_PART})?
DOUBLE_LITERAL_3=({DIGIT})+({EXPONENT_PART})
EXPONENT_PART=[Ee]["+""-"]?({DIGIT})*

STRING_LITERAL="'"([^\\\'\r\n]|{ESCAPE_SEQUENCE})*("'"|\\)?
ESCAPE_SEQUENCE=\\[^\r\n]

COMMAND_PREFIX=[:jletterdigit:]+
COMMAND_NAME=([:jletterdigit:]|\-)+
SHORT_COMMAND=\?
COMMAND=({COMMAND_PREFIX}":"{COMMAND_NAME})|{SHORT_COMMAND}

%%

{WHITE_SPACE_CHAR}+   { return TokenType.WHITE_SPACE; }

{INTEGER_LITERAL}     { return ConcordionTypes.INTEGER_LITERAL; }
{DOUBLE_LITERAL}      { return ConcordionTypes.DOUBLE_LITERAL; }
{STRING_LITERAL}      { return ConcordionTypes.STRING_LITERAL; }

{COMMAND} { return ConcordionTypes.COMMAND; }

"ExpectedToPass" { return ConcordionTypes.DICTIONARY; }
"ExpectedToFail" { return ConcordionTypes.DICTIONARY; }
"Unimplemented"  { return ConcordionTypes.DICTIONARY; }
"Default"        { return ConcordionTypes.DICTIONARY; }
"BestMatch"      { return ConcordionTypes.DICTIONARY; }
"KeyMatch"       { return ConcordionTypes.DICTIONARY; }
"key"            { return ConcordionTypes.DICTIONARY; }
"linked"         { return ConcordionTypes.DICTIONARY; }

{IDENTIFIER}     { return ConcordionTypes.IDENTIFIER; }
{EXAMPLE_NAME}   { return ConcordionTypes.EXAMPLE_NAME; }

"("  { return ConcordionTypes.LPARENTH; }
")"  { return ConcordionTypes.RPARENTH; }
"["  { return ConcordionTypes.LBRACKET; }
"]"  { return ConcordionTypes.RBRACKET; }
"{"  { return ConcordionTypes.LBRACE; }
"}"  { return ConcordionTypes.RBRACE; }

"."  { return ConcordionTypes.DOT; }
","  { return ConcordionTypes.COMA; }
"#"  { return ConcordionTypes.HASH; }

"="  { return ConcordionTypes.EQ; }
":"  { return ConcordionTypes.COLON; }
";"  { return ConcordionTypes.SEMICOLON; }

.    {  yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }