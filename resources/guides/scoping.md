# Guide pour implémenter le scoping

Un scope représente une région de code où un nom référence une valeur (variable). L'avantage d'avoir plusieurs scopes est qu'un même nom peut référencer plusieurs valeurs différentes.

Voici quelques exemples de code Dash montrant le scope:
```
// Pas dans un scope (global scope)
var x = 1;
print x; // 1

{ // Scope #1
    var y = 10;  
    print y; // 10
}  
  
{ // Scope #2
    var y = 20;  
    print y; // 20  
}
```

Un scope est défini par des accolades. Les variables définies dans un scope sont accessible seulement dans ce scope ou ces "sous-scopes":
```
{
	var message = "Hello";
	print message; // "Hello"
	
	{ // Sous-scope
		print message; // "Hello"
	}
}

print message; // Undefined variable 'message'.
```

Voici un exemple permettant la redéfinition d'une variable:
```
var message = "Hello";

{
	// la variable ici fait de "l'ombre" à celle du scope parent,
	// on appelle cela du "shadowing".
	var message = "Temporaire";
	print message; // "Temporaire"
}

print message; // "Hello"
```

Pour implémenter le scoping, vous allez devoir:
1. Modifier la syntaxe pour y ajouter "block". Je vous conseille de me faire valider votre syntaxe avant de poursuivre. Indice:
```
statement -> exprStmt | printStmt | block
block     -> ???
```
2. Modifier le Parser afin qu'il soit capable d'inclure les blocs dans l'AST (Abstract Syntax Tree). Pour effectuer les bonnes modifications, suivez la syntaxe que vous avez défini à l'étape 1.
3. Modifier votre `Environnement` afin de permettre le scoping.
4. Créer une nouvelle classe pour vos statements de blocs et y implémenter la méthode `interpret()`.
5. Permettre à l'`Interpréteur` d'interpréter un bloc.

