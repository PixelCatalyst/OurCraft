# Wymagania Projektu
Celem projektu jest opracowanie prostej gry 3D podobnej do "Minecraft". Zostaną zaimplementowane jedynie
podstawowe mechaniki renderingu i rozgrywki.

Podstawowe wymagania:

* Gra renderuje świat 3D przedstawiany z perspektywy pierwszoosobowej, oparty o sześcienne woksele(bloki)
* Woksele mogą być renderowane przy użyciu tekstur statycznych lub animowanych
* Świat gry generowany jest proceduralnie, w oparciu o zadane ziarno(seed)
* Generowany świat składa się z dwóch typów terenu: gór i równin. Niezależnie od typu terenu mogą pojawić się też grupy
drzew(lasy) i zbiorniki wodne. Świat nie posiada twardych ograniczeń wielkości, jedynie wysokość ograniczona jest do 128
wokseli
* Gracz ma możliwość dodawania i usuwania bloków w świecie gry
* Świat można zapisać do pliku, z uwzględnieniem zmian wprowadzonych przez gracza
* Czas w grze upływa zgodnie z czasem rzeczywistym. Dynamicznie zmienia się pora dnia, trwającego 20 minut.
Dzień trwa 10 minut i przechodzi w noc, która również trwa 10 minut
* Gra posiada system osiągnięć(achievements), do zdobycia możliwe są 3 podstawowe osiągnięcia

    1. Wykop 20 bloków ziemi
    2. Graj przez 20 minut, czyli jeden pełny dzień w grze
    3. Pokonaj dystans 50 bloków

***

* Rendering wokseli musi być zoptymalizowany pod kątem osiągnięcia płynności działania na poziomie 30-60 klatek na sekundę.
W tym celu powinny być renderowane jedynie widoczne woksele, poprzez zastosowanie odpowiednich mechanizmów:

    1. Frustum Culling - przetwarzane są jedynie obiekty w polu widzenia gracza
    2. Woksele tego samego typu są grupowane razem

* Generowanie świata musi być niezależne od procesu renderingu, tak aby spadek wydajności jednego z tych systemów
nie zaburzał działania drugiego
* Musi być zapewnione odpowiednie zarządzanie zasobami. Niewidoczne fragmenty świata powinny być dynamicznie zastępowane
nowymi, leżącymi w zasięgu wzroku gracza, tak aby nie dochodziło do wycieków pamięci i w efekcie do krytycznych awarii programu
* Typy wokseli(bloków) dostępne w grze to:

    1. Ziemia(dirt)
    2. Kamień(stone)
    3. Woda(water)
    4. Pień drzewa(wood)
    5. Liście(leaves)

    Jednocześnie struktury odpowiedzialne za woksele muszą być tak zaprojektowane, aby powyższą listę typów można
    było łatwo rozszerzyć.

***
[Powrót do dokumentów](Main.md)
