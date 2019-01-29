# Programação Concorrente Orientada a Objetos (em Java)

Simulação da gestão de transporte de contentores, através de comboios e navios, entre terminais ferroviários e docas.

![vscode_pcoo](https://i.gyazo.com/fc3cc5c77423bcdf025b1226609f2198.png)

## Simulação

Este software simula uma proposta para gerir contentores disponíveis em diversos terminas e docas, recorrendo a máquinas ferroviárias e/ou navios.

Existe uma entidade (**World**) que gera centros de controlo e operações (**CCO**), diversos terminais (**Terminal**) e docas (**Dock**), comboios (**Train**) e navios (**Ship**) e ainda contentores (**Container**) com distintos destinos.

O *núcleo* desta gestão é realizada essencialmente através dos centros de controlo e operações cujo as suas principais tarefas são atribuir permissões para um determinado transporte poder carregar e descarregar nas suas  origens e destinos (respetivamente).

Os centros de controlo e operações (i.e. CCO) contêm uma lista com infraestruturas que são geridas por eles e ainda uma lista de transportes também geridos por eles.
Nesta estrutura está implementada a lógica de exclusão mútua, por forma a garantir que não existem duas operações sobre o mesmo objeto, evitando assim que existam erros no decorrer da simulação.

As infraestruturas contém locais de carga e descarga, uma lista de contentores para serem carregados e uma lista de contentores descarregados, um CCO que a gere e um estado.

Os transportes contém um limite de carga máximo e uma velocidade (não necessáriamente iguais para os diversos transportes).
Cada um destes objetos contém indicadores sobre as permições atribuidas a eles (carregar, descarregar ou esperar), um estado, uma infraestrutura de origem e uma de destino, uma lista de contentores a ou para transportar e a sua posição.

## Fluxo da simulação (Ideia implementada)

1.
2.
3.
4.
5.
6.
...

## Notações a considerar

- a: add
- c: change
- g: get
    - gd: get destination
    - gl: get load
    - gm: get max
    - gs: get source
    - gu: get unload
    - gw: get wait
- gt: go to
- l: load
- m: max
- ns: no set
- r: remove
- s: set
    - sd: set destination
    - sl: set load
    - sm: set max
    - ss: set source
    - su: set unload
    - sw: set wait
- u: unload
- w: wait

## Lista de objetos e métodos disponíveis

1. CCO
    - slPermition(Transport transport)
    - suPermition(Transport transport)
    - sService(Transport transport)
    - gInfrastructures()
    - aInfrastructure(Infrastructure infrastructure)
    - rInfrastructure(Infrastructure infrastructure)
    - aTransports(Transport transport)
    - rTransports(Transport transport)
    - gTransports()

2. Container
    - sNumber(int number)
    - gNumber()
    - sDestination(Infrastructure infrastructure)
    - gDestination()
    - sTransport(Transport transport)
    - gTransport
    
3. Dock (estende Infrastructure)

4. Infrastructure
    - sName(String name)
    - gName()
    - slSpot(Spot spot)
    - glSpot()
    - suSpot(Spot spot)
    - guSpot()
    - slContainers(ArrayList<Container> lContainers)
    - glContainers()
    - suContainers(ArrayList<Container> uContainers)
    - guContainers()
    - sPosition(Position position)
    - gPosition
    - sClose(boolean tf)
    - gClose()
    - ghmContainers(Transport transport)
    - glPermition()
    - guPermition()
    - srlSpot(Transport transport)
    - grlSpot()
    - sruSpot(Transport transport)
    - gruSpot()
    - suContainers(Transport transport)
    - sLoad(Transport transport)
    - sUnload(Transport transport) 
    - alContainers(ArrayList<Container> containers)
    - sCCO(CCO cco)
    - gCCO()
    - finish()
  
5. Position
    - sX(int x)
    - gX()
    - sY(int y)
    - gY()
    - gDistance(Position position)
    
6. Ship (estende Transport)

7. Spot
    - sNumber(int number)
    - gNumber
    - sBusy(boolean busy)
    - gBusy()
    - sTransport(Transport transport)
    - gTransport()
    - sReserve(Transport transport)
    - gReserve()
    - aTransport(Transport transport)
    - rTransport()
    
8. Terminal (estende Infrastructure)
9. Train (estende Transport)
10. Transport
    - cDirection()
    - gtDestination()
    - sNumber(int number)
    - gNumber()
    - sSource(Infrastructure source)
    - gSource()
    - sDestination(Infrastructure destination)
    - gDestination
    - sPosition(Position  position)
    - gPosition
    - sState(String state)
    - gState()
    - sServices(boolean tf)
    - nsServices()
    - gServices()
    - ssServices(boolean tf)
    - gsServices()
    - sdServices(boolean tf)
    - gdServices()
    - slPermition(boolean tf)
    - glPermition()
    - suPermition(boolean tf)
    - guPermition()
    - swPermition(boolean tf)
    - gwPermition()
    - sContainers(Stack<Container> containers)
    - gContainers()
    - aContainer(Container contentor)
    - smContainers(int mContainers)
    - gmContainers()
    - sVelocity(int velocity)
    - gVelocity()
    - sCCO(CCO cco)
    - gCCO()
    - isFull()
    - isEmpty()
    
11. World

## Entidades ativas

1. World
2. CCO
3. Transport (consequentemente Train e Ship)

## Objetos partilhados

1. Infrastructure (consequentemente Terminal e Dock)
2. Spot

## Melhoramentos futuros

...

## TODO

* [ ] Criar World.java;
* [ ] Comentar Infrastructure.java; 
* [ ] Comentar Position.java;
* [ ] Comentar Spot.java;
* [ ] Comentar Transport.java;
* [ ] Comentar Train.java;
* [ ] Garantir que os comboios só carregam e descarregam em terminais (analogamente o mesmo processo para os navios <-> docas);

## Contribuidores

* [cfchenr](https://github.com/cfchenr)
