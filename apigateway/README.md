# Apigateway

Projeto responsavel pelo roteamento e balanceamento de carga entre os serviços.

## Dependências

Este projeto utiliza o servidor de configurações (config-server) para inicializar com configurações parametrizadas ou variáveis de ambiente.

Projeto:
[CONFIG-SERVER](https://github.com/nayaragaspar/config-server)

Este projeto também utiliza o registro de serviços (eureka-server) para descobrir os seviços a serem acessados.

Projeto:
[EUREKA-SERVER](https://github.com/nayaragaspar/eureka-server)

Os dois projetos podem ser executados localmente, pelo docker-compose presente no projeto confi-server ou pelos Dockerfile's presentes em cada projeto.
Caso esses projetos sejam executados pelo docker, executar este projeto também pelo Dockerfile para acessar a rede criada entre os containeres.

```
  docker-compose -f docker-compose-with-eureka.yml up -d --build
```
