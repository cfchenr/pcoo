# Programação Concorrente Orientada a Objetos (em Java)

Simulação da gestão de transporte de contentores, através de comboios e navios, entre terminais ferroviários e docas.

<img src="https://i.gyazo.com/fc3cc5c77423bcdf025b1226609f2198.png" alt="vscode_pcoo" width="100%"/>


## Simulação

Este software simula uma proposta para gerir contentores disponíveis em diversos terminas e docas, recorrendo a máquinas ferroviárias, navios e camiões.

Existe uma entidade (**World**) que gera centros de controlo e operações (**CCO**), diversos terminais (**Terminal**) e docas (**Dock**), comboios (**Train**), navios (**Ship**), camiões (**Truck**) e ainda contentores (**Container**) com distintos destinos.

O *núcleo* desta gestão é realizada essencialmente através dos centros de controlo e operações cujo as suas principais tarefas são atribuir permissões para um determinado transporte poder carregar e descarregar nas suas  origens e destinos (respetivamente).

Os centros de controlo e operações (i.e. CCO) contêm uma lista com infraestruturas que são geridas por eles e ainda uma lista de transportes também geridos por eles.
Nesta estrutura está implementada a lógica de exclusão mútua, por forma a garantir que não existem duas operações sobre o mesmo objeto, evitando assim que existam erros no decorrer da simulação.

As infraestruturas contém locais de carga e descarga, uma lista de contentores para serem carregados e uma lista de contentores descarregados, um CCO que a gere e um estado.

Os transportes contém um limite de carga máximo e uma velocidade (não necessáriamente iguais para os diversos transportes).
Cada um destes objetos contém indicadores sobre as permições atribuidas a eles (carregar, descarregar ou esperar), um estado, uma infraestrutura de origem e uma de destino, uma lista de contentores a ou para transportar e a sua posição.

## Interface

A interface desta simulação foi criada com recurso à biblioteca [Processing](https://processing.org/).

```
import processing.core.*;
import processing.event.*;
    
public class programTest extends PApplet {

    ...

    public void settings() {

        ...

    }

    public void draw() {

        ...

    }

    public void mouseClicked() {

        ...

    }

    class Button {

        ...

    }

    public static void main(String[] args) {

        PApplet.main(new String[] { programTest.class.getName() });

        ...

    }

    ...

}
```

<table>
    <tr>
        <td>
            <img src="https://i.gyazo.com/7a2b55dd97ecae6d6ed421d088ed321e.png" alt="interface_1" width="100%"/>
        </td>
        <td>
            <img src="https://i.gyazo.com/e81f5c000c9e654308645b1abf30907a.png" alt="interface_2" width="100%"/>
        </td>
    </tr>
</table>

## Bibliotecas externas

- [Processing](https://processing.org/)
- [pt.ua.concurrent](http://sweet.ua.pt/mos/pt.ua.concurrent/index.xhtml)
- [WriteLogFile](https://github.com/cfchenr/WriteLogFile)

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

## Lista de objetos, construtores e métodos disponíveis

1. CCO
    <details><summary>Construtores</summary>
    <ul>
        <li>CCO()</li>
    </ul>
    </details>
    <details><summary>Métodos</summary>
    <ul>
        <li>run()</li>
        <li>slPermition(Transport transport)</li>
        <li>suPermition(Transport transport)</li>
        <li>sService(Transport transport)</li>
        <li>gInfrastructures()</li>
        <li>aInfrastructure(Infrastructure infrastructure)</li>
        <li>rInfrastructure(Infrastructure infrastructure)</li>
        <li>aTransports(Transport transport)</li>
        <li>rTransports(Transport transport)</li>
        <li>gTransports()</li>
    </ul>
    </details>

2. Container
    <details><summary>Construtores</summary>
    <ul>
        <li>Container(int number, Infrastructure destination)</li>
    </ul>
    </details>
    <details><summary>Métodos</summary>
    <ul>
        <li>sNumber(int number)</li>
        <li>gNumber()</li>
        <li>sDestination(Infrastructure infrastructure)</li>
        <li>gDestination()</li>
        <li>sTransport(Transport transport)</li>
        <li>gTransport()</li>
    </ul>
    </details>
    
3. Dock (estende Infrastructure)
    <details><summary>Construtores</summary>
    <ul>
        <li>Dock(String name, int x, CCO cco)</li>
    </ul>
    </details>

4. Infrastructure
    <details><summary>Construtores</summary>
    <ul>
        <li>Infrastructure(String name, int x, CCO cco)</li>
    </ul>
    </details>
    <details><summary>Métodos</summary>
    <ul>
        <li>sName(String name)</li>
        <li>gName()</li>
        <li>slSpot(Spot spot)</li>
        <li>glSpot()</li>
        <li>suSpot(Spot spot)</li>
        <li>guSpot()</li>
        <li>slContainers(ArrayList<Container> lContainers)</li>
        <li>glContainers()</li>
        <li>suContainers(ArrayList<Container> uContainers)</li>
        <li>guContainers()</li>
        <li>sPosition(Position position)</li>
        <li>gPosition()</li>
        <li>sClose(boolean tf)</li>
        <li>gClose()</li>
        <li>ghmContainers(Transport transport)</li>
        <li>glPermition()</li>
        <li>guPermition()</li>
        <li>srlSpot(Transport transport)</li>
        <li>grlSpot()</li>
        <li>sruSpot(Transport transport)</li>
        <li>gruSpot()</li>
        <li>suContainers(Transport transport)</li>
        <li>sLoad(Transport transport)</li>
        <li>sUnload(Transport transport)</li>
        <li>alContainers(ArrayList<Container> containers)</li>
        <li>sCCO(CCO cco)</li>
        <li>gCCO()</li>
        <li>sType(String type)</li>
        <li>gType()</li>
    </ul>
    </details>

5. Position
    <details><summary>Construtores</summary>
    <ul>
        <li>Position(int x)</li>
        <li>Position(int x, int y)</li>
    </ul>
    </details>
    <details><summary>Métodos</summary>
    <ul>
        <li>sX(int x)</li>
        <li>gX()</li>
        <li>sY(int y)</li>
        <li>gY()</li>
        <li>gDistance(Position position)</li>
    </ul>
    </details>
    
6. Ship (estende Transport)
    <details><summary>Construtores</summary>
    <ul>
        <li>Ship(int number, Infrastructure source, Infrastructure destination, CCO cco)</li>
    </ul>
    </details>

7. Spot
    <details><summary>Construtores</summary>
    <ul>
        <li>Spot(int number)</li>
    </ul>
    </details>
    <details><summary>Métodos</summary>
    <ul>
        <li>sNumber(int number)</li>
        <li>gNumber
        <li>sBusy(boolean busy)</li>
        <li>gBusy()</li>
        <li>sTransport(Transport transport)</li>
        <li>gTransport()</li>
        <li>sReserve(Transport transport)</li>
        <li>gReserve()</li>
        <li>aTransport(Transport transport)</li>
        <li>rTransport()</li>
    </ul>
    </details>
    
8. Terminal (estende Infrastructure)
    <details><summary>Construtores</summary>
    <ul>
        <li>Terminal(String name, int x, CCO cco)</li>
    </ul>
    </details>

9. Train (estende Transport)
    <details><summary>Construtores</summary>
    <ul>
        <li>Train(int number, Infrastructure source, Infrastructure destination, CCO cco)</li>
    </ul>
    </details>

10. Transport
    <details><summary>Construtores</summary>
    <ul>
        <li>Transport(int number, Infrastructure source, Infrastructure destination, CCO cco)</li>
    </ul>
    </details>
    <details><summary>Métodos</summary>
    <ul>
        <li>run()</li>
        <li>cDirection()</li>
        <li>gtDestination()</li>
        <li>sNumber(int number)</li>
        <li>gNumber()</li>
        <li>sSource(Infrastructure source)</li>
        <li>gSource()</li>
        <li>sDestination(Infrastructure destination)</li>
        <li>gDestination
        <li>sPosition(Position  position)</li>
        <li>gPosition
        <li>sState(String state)</li>
        <li>gState()</li>
        <li>sServices(boolean tf)</li>
        <li>nsServices()</li>
        <li>gServices()</li>
        <li>ssServices(boolean tf)</li>
        <li>gsServices()</li>
        <li>sdServices(boolean tf)</li>
        <li>gdServices()</li>
        <li>slPermition(boolean tf)</li>
        <li>glPermition()</li>
        <li>suPermition(boolean tf)</li>
        <li>guPermition()</li>
        <li>swPermition(boolean tf)</li>
        <li>gwPermition()</li>
        <li>sContainers(Stack<Container> containers)</li>
        <li>gContainers()</li>
        <li>aContainer(Container contentor)</li>
        <li>rContainer()</li>
        <li>smContainers(int mContainers)</li>
        <li>gmContainers()</li>
        <li>sVelocity(int velocity)</li>
        <li>gVelocity()</li>
        <li>sCCO(CCO cco)</li>
        <li>gCCO()</li>
        <li>sType(String type)</li>
        <li>gType()</li>
        <li>isFull()</li>
        <li>isEmpty()</li>
    </ul>
    </details>
  
11. Truck (estende Transport)
    <details><summary>Construtores</summary>
        <ul>
            <li>Truck(int number, Infrastructure source, Infrastructure destination, CCO cco)</li>
        </ul>
    </details>

12.  World

## Entidades ativas

1. World
2. CCO
3. Transport (consequentemente Train, Ship e Truck)

## Objetos partilhados

1. Infrastructure (consequentemente Terminal e Dock)
2. Spot

## Contribuidores

* [cfchenr](https://github.com/cfchenr)
