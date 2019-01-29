# Programação Concorrente Orientada a Objetos (em Java)

Simulação da gestão de transporte de contentores, através de comboios e navios, entre terminais ferroviários e docas.

![vscode_pcoo](https://i.gyazo.com/fc3cc5c77423bcdf025b1226609f2198.png)


## Simulação

Este software simula uma proposta para gerir contentores disponíveis em diversos terminas e docas, recorrendo a máquinas ferroviárias e/ou navios.

Existe uma entidade (**World**) que gera centros de controlo e operações (**CCO**), diversos terminais (**Terminal**) e docas (**Dock**), comboios (**Train**) e navios (**Ship**) e ainda contentores (**Container**) com distintos destinos.

O *núcleo* desta gestão é realizada essencialmente através dos centros de controlo e operações cujo as suas principais tarefas são atribuir permissões para um determinado transporte poder carregar e descarregar nas suas  origens e destinos (respetivamente).

## Fluxo da simulação (Ideia implementada)

1.
2.
3.
4.
5.
6.
...

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
