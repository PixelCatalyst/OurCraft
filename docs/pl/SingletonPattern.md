# Singleton Design Pattern

**Singleton** to _kreacyjny_ wzorzec polegający na ograniczeniu możliwości tworzenia obiektów klasy do wyłącznie jednej instancji. Singleton zapewnia globalny dostęp do stworzonej instancji objektu.

## Motywacja
Wzorzec singletonu jest używany w sytuacjach, gdy nie potrebujemy tworzyć wielu objektów klasy, lecz ważny jest dostęp do jej funkcjonalności z całej aplikacji. Przykładem może być objekt dziennika, który powinien być dostępny dla każdego komponentu aplikacji. Wzorzec ten jest często wykorzystywany do implementacji innych wzorców takich jak: _builder_, _prototyp_ czy _fabryka abstrakcyjna_.  

W grze OurCraft singleton jest wykorzystywany w klasach _GUIFactory_ oraz _FileManager_, które zapewniają funkcjonalności niezbędne dla całej aplikacji wykorzystywane w wielu jej komponentach.  
_GUIFactory_ jest fabryką do tworzenia elementów interfejsu graficznego, natomiast _FileManager_ ułatwia odczyt plików oraz tworzenie objektów tekstur.

## Implementacja
Implementacja tego wzorca projektowego jest prosta i polega na przechowywaniu statycznej referencji do instancji singletonu oraz stworzeniu statycznej metody klasy `getInstance()`, która sprawdza czy istnieje już objekt tej klasy i w razie potrzeby go tworzy. 

Przykładowa implementacja klasy _Singleton_:
```java
public class Singleton {
    private static Singleton instance = null;

	public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null)
                    instance = new Singleton();
            }
        }
        return instance;
    }
}
```

W powyższym przykładzie zastosowano blok `synchronized` oraz dwukrotne sprawdzanie warunku `instance == null` w celu zapewnienia poprawnego działania przy wykorzystaniu wielowątkowości w&nbsp;Javie.
