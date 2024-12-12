# TaxiApp 🚕

**TaxiApp** é um projeto Android desenvolvido em Kotlin que simula a solicitação e gerenciamento de viagens por meio de uma interface intuitiva e recursos avançados, como integração com APIs de mapas e gerenciamento de histórico local. O objetivo é oferecer uma experiência interativa, abrangendo a simulação de cálculo de rotas, estimativa de custos e navegação entre diferentes telas.

## 🌟 Funcionalidades

- **Estimativa de Viagem**: Cálculo de rotas entre origem e destino com base em endereços ou locais pré-definidos.
- **Exibição de Opções de Motoristas**: Lista de motoristas disponíveis com informações detalhadas, como veículo, avaliação, e preço estimado.
- **Confirmação de Viagem**: Envio de dados para API para confirmar uma viagem simulada.
- **Histórico de Viagens**: Visualização e filtro de viagens realizadas, com armazenamento local.
- **Splash Screen**: Tela inicial para introdução do aplicativo.
- **Mapa Interativo**: Exibição de rotas e marcadores com o Google Maps.
- **Integração com API**: Uso de APIs externas para cálculo de rotas, estimativa de custos e exibição de motoristas.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **Bibliotecas e Frameworks**:
  - Google Maps SDK
  - OkHttp para chamadas HTTP
  - Glide para carregamento de imagens
- **Arquitetura**: MVVM simplificada
- **Componentes Android**:
  - RecyclerView
  - SharedPreferences
  - Activity e Fragment
- **API Externa**:
  - Google Maps Directions API
  - Endpoints simulados para estimativa e confirmação de viagens

---

## 📂 Estrutura do Projeto

### Diretórios Principais

```plaintext
├── MainActivity.kt            # Tela principal com funcionalidades de mapa e estimativa
├── HistoryScreen.kt           # Tela de histórico de viagens
├── TripOptionsAdapter.kt      # Adapter para exibição das opções de motoristas
├── TripHistoryAdapter.kt      # Adapter para exibição do histórico de viagens
├── SplashActivity.kt          # Tela inicial do aplicativo
├── models/                    # Modelos de dados, como Place e Trip
├── res/                       # Recursos visuais e layouts
├── api/                       # Configurações de chamadas para APIs externas

⚙️ Como Executar

Pré-requisitos
Android Studio instalado
Chave de API do Google Maps:
Adicione sua chave no arquivo values/strings:
MY_GOOGLE_API=YOUR_API_KEY

Passos
1- Clone o repositório
 -> git clone https://github.com/seu-usuario/taxiapp.git

2- Abra o projeto no Android Studio.
3- Sincronize as dependências (Gradle).
4- Configure a chave do Google Maps no arquivo.AndroidManifest.xml:
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="@string/MY_GOOGLE_API" />

5- Execute o aplicativo em um dispositivo ou emulador Android.

🔗 Endpoints Utilizados

Estimativa de Viagem
URL: https://xd5zl5kk2yltomvw5fb37y3bm40vsyrx.lambda-url.sa-east-1.on.aws/ride/estimate
Método: POST
Parâmetros: customer_id, origin, destination
Confirmação de Viagem
URL: https://xd5zl5kk2yltomvw5fb37y3bm40vsyrx.lambda-url.sa-east-1.on.aws/ride/confirm
Método: PATCH
Histórico de Viagens
URL: https://xd5zl5kk2yltomvw5fb37y3bm40vsyrx.lambda-url.sa-east-1.on.aws/ride/{userId}
Método: GET

(obtive algumas dificuldades no acesso ao banco de dados no cenario onde seria 1 motorista api não estava, obtive tambem um problema no filtro onde a Api não me retornava corretamente )
Mas fiz com muita dedicação e empenho, faltou alguns requisitos para entregar como testes unitarios com junit/mockito por conta destas dificuldades que mencionei acima ficou um pouco dificil aplica-lo, porem um desafio bacana aprendi muito com ele e serve muito para meu crescimento pessoal,
Obrigado pela oportunidade espero que gostem tanto como eu gostei de efetua-lo. (espero conversarmos sobre este teste foi magnifico me empenhar nele).  

🎨 Layouts

Principais Telas
Tela Principal: Mapa interativo, entrada de dados e opções de motoristas.
Tela de Histórico: Visualização e filtro de viagens armazenadas.


Contato

Dúvidas ou sugestões? Entre em contato:

E-mail: kozawu321@gmail.com / robson.jso@hotmail.com
LinkedIn: Robson Santos - https://www.linkedin.com/in/robson-santos-525721149/

