## Public Transit System

### Overview

This project aim to design a public transit system with subways and buses. The system tracks and calculates fares for anyone who uses a travel card to enter and exit the system, and it also plans trips in regards to numbers of terminals.

### How-to-use

There are two ways to configure the system.

1. Run main and register an administrater account for which provide an option to construct the transitsystem.

2. Use configuration files (Users.txt, Terminals.txt and Events.txt). These configuration files initialize the transit system with specified users, cards, and terminals (transitlines). 

#### Users.txt

This is file for all initializations of User and Card object. In this file, each line is a single input.
We assume that the first input is always a User. Every Card input under a User input belongs to the User.

Input follows the following format:

``` Java
User(<userEmail>, <userName>)
Card(<cardId>)
```

Example:
``` Java
User(Heather@canada.com, Heather Brown)
Card(Card 1)
Card(Card 2)
Card(10086)
User(Donald@email.com, Donald Spencer)
Card(123456789)
In this case, User[Heather@canada.com] is initiated with
Card[Card 1], Card[Card 2] and Card[10086] with a default balance.
Also User[Donald@email.com] is initiated with Card[123456789].
```

Note that cardId cannot contain any comma ",", and every cardId should be unique.



#### Terminals.txt

This is the file for all BusLine and SubwayLine input. In this file, multiple lines can be a single input.

Input follows the following format:
``` Java
<transitLineName>
<terminalName> | <terminalName> | <terminalName>
<EmptyLine>
<transitLineName>
<terminalName> | <terminalName> | <terminalName> | <terminalName>
```
Example:
``` Java
Subway 1
Finch | North York Center | York Mills | Bloor | College | Queens Park | Spadina

Bus 1
Michigan Ave | The Fifth Ave | Los Angeles | New York | Bloor | Lawrence Ave West

Bus 2
St Clair | Young | New York | Glencair
```
In this example, transitLineType is Subway and Bus. transitLineId is the integer following the transitLineType.

Note that `<transitLineName>` all begin with either "Subway" or "Bus".
There must be `<emptyline>` in between each transit line.



#### Events.txt

This is the file for all user-related input. In this file, each line is a single input.

Input supported and their format:
``` Java
Entry(<cardId>, <transitLine>, <terminalName>, <EntranceDateAndTime>)
Exit(<cardId>, <transitLine>, <terminalName>, <ExitDateAndTime>)
viewRecentTrips(<cardId>)
addMoney(<cardId>, <amount>)
viewBalance(<cardId>)
addCard(<userEmail>, <cardId>)
removeCard(<userEmail>, <cardId>)
changeName(<userEmail>, <newName>)
getUserName(<userEmail>)
viewUserAllCards(<userEmail>)
viewAllBusLines()
viewAllSubwayLines()
viewAllTransitLines()
resetSystem()
getReport()
```
Note that the the parameter <(Entrance/Exit)DateAndTime> must follow the format of `yyyy/MM/dd HH:mm:ss`.

Example:

``` Java
Entry(Card1, Subway 1, Finch, 2018/10/29 18:10:45)
Exit(Card1, Subway 1, Spadina, 2018/10/29 19:20:12)
viewRecentTrips(Card1)
viewBalance(Card1)
addMoney(Card2, 1.01)
addCard(Anonymous@canada.com, 10086)
removeCard(Anonymous@canada.com, 10086)
viewAllTransitLines()
resetSystem()
```

