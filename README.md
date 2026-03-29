# manu — Aplicativo Android

Aplicativo Android nativo do sistema manu, desenvolvido para gestores
e solicitantes de manutencao. Permite abertura de chamados com captura
automatica de localizacao via GPS, gerenciamento de chamados, ordens
de servico, empresas e profissionais diretamente pelo celular.

---

## Tecnologias Utilizadas

- **Kotlin** — linguagem principal
- **Jetpack Compose** — UI declarativa nativa
- **Material Design 3** — design system
- **Firebase Auth** — autenticacao de gestores
- **Retrofit 2** — cliente HTTP para consumo da API REST
- **OkHttp 4** — interceptors e logging
- **FusedLocationProviderClient** — captura de GPS
- **Geocoder** — conversao de coordenadas em endereco legivel
- **Navigation Compose** — navegacao entre telas
- **ViewModel + StateFlow** — gerenciamento de estado
- **Coroutines** — operacoes assincronas
- **GitHub Actions** — CI (build automatico)

---

## Funcionalidades

### Solicitante (sem login)
- Abertura de chamados de manutencao
- Captura automatica de localizacao via GPS
- Conversao das coordenadas em endereco legivel
- Selecao de prioridade (Baixa, Normal, Alta)

### Gestor (autenticado)
- Login com email e senha via Firebase Auth
- Listagem de chamados com badges de prioridade
- Visualizacao do detalhe de cada chamado
- Geracao de Ordem de Servico a partir de um chamado
- Listagem de Ordens de Servico com status
- Cadastro, edicao e remocao de empresas
- Cadastro, edicao e remocao de profissionais
- Gestao de funcoes dos profissionais

---

## Requisitos

- Android Studio Hedgehog ou superior
- Android SDK 26 (minSdk) ou superior
- Kotlin 2.x
- Dispositivo ou emulador com Android 8.0+
- Arquivo `google-services.json` do projeto Firebase (nao incluso no repositorio)
- Backend manu-api rodando localmente ou em producao

---

## Como Rodar Localmente

### 1. Clone o repositorio

```bash
git clone https://github.com/MadsonOl/manu-app.git
cd manu-app
```

### 2. Adicione o `google-services.json`

Baixe o arquivo `google-services.json` no console do Firebase
(Configuracoes do projeto > Seus aplicativos > Android)
e coloque na pasta `app/`.

### 3. Configure a URL do backend

Em `app/src/main/java/com/manu/manu_app/data/remote/RetrofitClient.kt`,
verifique se a `BASE_URL` aponta para o ambiente correto:

```kotlin
// Producao
private const val BASE_URL = "https://manu-backendp0bk.onrender.com/"

// Local (substitua se necessario)
// private const val BASE_URL = "http://10.0.2.2:8000/"
```

> **Nota:** no emulador Android, use `10.0.2.2` para acessar o localhost da maquina host.

### 4. Abra no Android Studio

1. Abra a pasta raiz `manu-app` no Android Studio.
2. Aguarde a sincronizacao do Gradle.
3. Clique em **Run** para rodar no emulador ou dispositivo conectado.

---

## Permissoes Necessarias

O app solicita as seguintes permissoes em tempo de execucao:

- `ACCESS_FINE_LOCATION` — captura de GPS para localizacao do chamado
- `ACCESS_COARSE_LOCATION` — localizacao aproximada como fallback

---

## Estrutura de Pastas

```
app/src/main/java/com/manu/manu_app/
├── data/
│   ├── model/          # Data classes (ChamadoRequest, ChamadoResponse, etc.)
│   ├── remote/         # Retrofit: ApiService e RetrofitClient
│   └── repository/     # Repositories: Auth, Chamado, Ordem, Empresa, Profissional
├── ui/
│   ├── navigation/     # NavGraph, Routes, GestorScaffold, BottomNavItem
│   ├── screens/        # Telas organizadas por funcionalidade
│   │   ├── home/
│   │   ├── solicitante/
│   │   ├── login/
│   │   ├── chamados/
│   │   ├── ordens/
│   │   ├── empresas/
│   │   └── profissionais/
│   └── theme/          # Color, Theme, Type
└── viewmodel/          # ViewModels por funcionalidade
```

---

## Autenticacao

O app utiliza **Firebase Authentication** com email e senha.

**Fluxo:**
1. Gestor faz login na tela de acesso
2. Firebase retorna um ID Token JWT
3. O token e injetado automaticamente em todas as requisicoes autenticadas
   via `AuthInterceptor` no `RetrofitClient`
4. Endpoints publicos (abertura de chamado) nao requerem token

---

## Backend

Este app consome a API REST do **manu-backend**:
https://github.com/MadsonOl/manu-backend

**URL de producao:** https://manu-backendp0bk.onrender.com

> **Nota:** o backend roda no plano gratuito do Render. A primeira requisicao
> apos um periodo de inatividade pode levar ate 60 segundos para responder
> enquanto o servidor inicializa. As requisicoes seguintes sao normais.

---

## CI/CD

O repositorio possui pipeline de build automatico via **GitHub Actions**.
A cada push na branch `main`, o workflow verifica se o projeto compila
sem erros.

> Coloque o arquivo `google-services.json` no repositorio apenas se
> o repositorio for privado. Para repositorios publicos, use
> **GitHub Secrets** para injetar o conteudo do arquivo no pipeline.

---

## Relacionados

- **manu-backend:** https://github.com/MadsonOl/manu-backend
- **manu-frontend:** https://github.com/MadsonOl/manu-frontend
