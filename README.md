# WebServer

Com o intuito de facilitar a geração do arquivo `WebServer.jar`, para execução, os arquivos utilizados pelo relatório foram adicionados dentro do `.jar`.
Caso haja o desejo de realizar alguma modificação nos arquivos do relatório os mesmos podem ser modificados dentro do diretório `project/src/main/external-resources`, porém, uma nova versão do `.jar` deverá ser gerada.


## Rodando o trabalho
Para iniciar a aplicação basta carregar o `.jar` através do comando `java -jar .\WebServer.jar`


## Gerando uma nova versão do WebServer.jar
 o comando `mvn package` do maven gera uma nova versão. O mesmo deve ser executado dentro do diretório `project`, gerando uma nova versão disponível em `project/target`