# Przypadki użycia

W poniższych scenariuszach terminy "Gracz" i "Użytkownik" używane są zamiennie.\
Scenariusze obejmujące wymagania funkcjonalne:

* __Rozpoczęcie gry__ (UC-01)

    Scenariusz główny:
    1. Użytkownik uruchamia grę
    2. Gra wyświetla menu główne
    3. W menu głównym użytkownik wybiera opcję rozpoczęcia gry
    4. Użytkownik wprowadza w widocznym polu tekstowym ziarno generatora świata, następnie zatwierdza rozpoczęcie gry
       przyciskiem poniżej
    5. Gra rozpoczyna generowanie losowego świata na podstawie wcześniej podanego ziarna

    Scenariusz alternatywny 1 - gracz nie wprowadza ziarna:
    * 1-3. Jak w sc. głównym
    4. Użytkownik zatwierdza rozpoczęcie gry bez podawania ziarna, więc gra wybiera je losowo
    5. Jak w sc. głównym

    Scenariusz alternatywny 2 - gracz wczytuje zapisaną grę:
    * 1-2. Jak w sc. głównym
    3. W menu głównym użytkownik wybiera opcję kontynuowania gry
    4. Gra ładuje świat z zapisanego wcześniej pliku
    5. Jak w sc. głównym (dla nowo odkrytych fragmentów świata)

    Scenariusz alternatywny 3 - gracz wczytuje grę, ale plik zapisu nie istnieje:
    * 1-2. Jak w sc. głównym
    3. W menu głównym użytkownik wybiera opcję kontynuowania gry
    4. Plik zapisu nie istnieje, więc gra wyświetla komunikat "brak zapisanych gier"
    5. Powrót do kroku 2.

* __Cykl gry__ (UC-02)

    Scenariusz główny:
    1. Po rozpoczęciu gry, gracz wchodzi w interakcje ze światem na następujące sposoby:
        * 1.a Dodanie bloku ->_UC-03_
        * 1.b Usunięcie bloku ->_UC-04_
        * 1.c Poruszanie się ->_UC-05_

* __Dodanie bloku__ (UC-03)

    Scenariusz główny:
    1. Gra wyświetla listę dostępnych bloków na dole ekranu
    2. Gracz wybiera dany typ bloku za pomocą kółka myszki
    3. Gra wyświetla wskaźnik położenia nowego bloku.\
       Bloki można umieszczać tylko w bezpośrednim sąsiedztwie innych bloków, więc wskaźnik jest podświetleniem bloku,
       na który skierowana jest kamera gracza
    4. Gracz nakierowuje wskaźnik na wybraną pozycję
    5. Gracz naciska lewy przycisk myszy, aby zatwierdzić umieszczenie bloku w świecie gry
    6. Gra umieszcza wybrany typ bloku na wybranej pozycji

    Scenariusz alternatywny 1 - nieprawidłowa pozycja
    * 1-5 Jak w sc. głównym
    5. a. Gracz znajduje się zbyt daleko od wybranej pozycji(dalej niż 3 bloki-jednostki)
    6. Świat gry pozostaje niezmieniony, gra informuje o niepowodzeniu przez usunięcie wskaźnika

* __Usunięcie bloku__ (UC-04)

    Scenariusz główny:
    1. Gra wyświetla wskaźnik - podświetla blok, na który skierowana jest kamera gracza
    2. Gracz nakierowuje wskaźnik na wybraną pozycję
    3. Gracz potwierdza usunięcie wybranego bloku przez naciśnięcie prawego przycisku myszy
    4. Gra usuwa blok z wybranej pozycji

    Scenariusz alternatywny 1 - nieprawidłowa pozycja
    * 1-3 Jak w sc. głównym
    3. a. Gracz znajduje się zbyt daleko od wybranej pozycji(dalej niż 3 bloki-jednostki)
    4. Świat gry pozostaje niezmieniony, gra informuje o niepowodzeniu przez usunięcie wskaźnika

* __Poruszanie się__ (UC-05)

    Scenariusz główny:
    1. Gracz wybiera kierunek ruchu przez naciśnięcie odpowiednich klawiszy
        * 1.a 'W' - ruch do przodu
        * 1.b 'S' - ruch do tyłu
        * 1.c 'A' - ruch w lewo
        * 1.d 'D' - ruch w prawo
        * 1.e Spacja - skok
    2. Gra zmienia pozycję gracza zgodnie z jego wyborem

    Scenariusz alternatywny 1 - nieprawidłowy ruch
    1. Jak w sc. głównym
    2. Gra wykrywa nieprawidłowy ruch - kolizję gracza z otoczeniem
    3. Pozycja gracza pozostaje niezmieniona

* __Otrzymanie osiągnięcia__ (UC-06)

    Scenariusz główny:
    1. Gra śledzi(zapisuje) interakcje podejmowane przez gracza(dodawanie/usuwanie bloków, przemieszczanie się)
    2. Po spełnieniu wymagań jednego z określonych osiągnięć(patrz [wymagania](Requirements.md)) gra wyświetla w prawym
       górnym rogu ekranu komunikat w postaci "zdobyto osiągnięcie {nazwa osiągnięcia}"
    3. Komunikat znika po pięciu sekundach

    Scenariusz alternatywny 1 - komunikat już obecny na ekranie
    1. Jak w sc. głównym
    2. Jak w sc. głównym, ale jeśli na ekranie jest już komunikat dotyczący innego osiągnięcia, to nowy komunikat
       zostaje wyświetlony zaraz pod tym już obecnym
    3. Jak w sc. głównym

* __Zmiana pory dnia__ (UC-07)

    Scenariusz główny:
    1. Gra wyświetla tło(niebo) o kolorze/jasności odpowiadającej porze dnia
    2. Gra rozpoczyna rejestrowanie upływu czasu
    3. Gra przez 10 minut wyświetla kolor tła odpowiadający za dzień
    4. Gra przez 10 minut wyświetla kolor tła odpowiadający za noc
    5. Powrót do kroku 3.

* __Zakończenie gry__ (UC-08)

    Scenariusz główny:
    1. Gracz naciska klawisz Escape
    2. Gra wyświetla tekst z zapytaniem o zapisanie gry
        * 2.a Gracz naciska klawisz 'S', aby potwierdzić zapis
            * 2.a1 Gra dokonuje zapisu świata do pliku
        * 2.b Gracz nacisk ponownie klawisz Escape, aby odmówić zapisu
    3. Gra powraca do wyświetlania menu głównego

    Scenariusz alternatywny 1 - gracz rezygnuje z zakończenia gry:
    * 1-2. Jak w sc. głównym
    * 2.c Gracz naciska inny klawisz niż Escape lub 'S'
    3. Gra anuluje zakończenie i powraca do cyklu rozgrywki

***
[Powrót do dokumentów](Main.md)
