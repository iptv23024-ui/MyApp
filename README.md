# MyApp - Gerenciador de Licenças de Clientes

Um aplicativo Android completo e profissional para gerenciar clientes e suas licenças de apps, com banco de dados SQLite integrado, notificações automáticas de expiração e interface intuitiva.

## 🎯 Funcionalidades Principais

### ✅ Gerenciamento de Clientes
- Adicionar, editar, visualizar e deletar clientes
- Armazenamento seguro em banco de dados SQLite
- Interface intuitiva com CardView responsivo

### 📱 Tipos de Apps Suportados
- Bob Player
- IBOPlayer
- IBOPro
- Smart One
- Opção de digitar app customizado

### 🔐 Informações de Licença
- **MAC Address** - Identificador do dispositivo
- **Device Key** - Chave de autenticação
- **Data de Validade** - Seletor de data integrado
- **Status Automático** - Cálculo inteligente de dias até expiração

### 🔔 Sistema de Notificações
O app monitora automaticamente o status das licenças:

| Status | Cor | Descrição |
|--------|-----|-----------|
| ✅ Ativo | Verde | Licença válida com mais de 30 dias |
| ⚠️ Aviso | Laranja | Licença expira em até 30 dias |
| ❌ Expirado | Vermelho | Licença já expirou |

### 📊 Visualização de Dados
- Lista de clientes com cards informativos
- Indicador visual do status da licença
- Detalhes completos ao clicar no cliente
- Botões de ação rápida (editar, deletar)

## 🔧 Tecnologias Utilizadas

- **Linguagem**: Java
- **Banco de Dados**: SQLite
- **Interface**: Material Design
- **Componentes**: RecyclerView, CardView, FloatingActionButton
- **CI/CD**: GitHub Actions

## 📋 Requisitos

- Java JDK 11+
- Android SDK 21+ (Android 5.0)
- Android Studio (recomendado)

## 🚀 Como Compilar

### Compilação Local

#### Linux/Mac:
```bash
./gradlew assembleDebug
```

#### Windows:
```bash
gradlew.bat assembleDebug
```

O APK será gerado em:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Compilação Release (Assinado):
```bash
./gradlew assembleRelease
```

## 📦 Build Automático com GitHub Actions

O projeto está configurado com **GitHub Actions** para compilação automática!

A cada **push** ou **pull request** na branch `main`:
1. ✅ O código é compilado automaticamente
2. 📦 APK é gerado (Debug e Release)
3. 💾 Artefatos são salvos para download

### Como Baixar o APK:

1. Acesse **Actions** no repositório do GitHub
2. Selecione a **última execução**
3. Na seção **Artifacts**, clique em:
   - `app-debug` para a versão de testes
   - `app-release` para versão final

## 📱 Instalação no Dispositivo

### Usando ADB:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Manualmente:
1. Transfira o APK para o dispositivo
2. Abra o gerenciador de arquivos
3. Localize e toque no APK
4. Autorize a instalação

## 🗂️ Estrutura do Projeto

```
MyApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/myapp/
│   │   │   ├── MainActivity.java           # Tela principal
│   │   │   ├── AddClientActivity.java      # Adicionar/Editar cliente
│   │   │   ├── ClientDetailsActivity.java  # Visualizar detalhes
│   │   │   ├── Client.java                 # Modelo de dados
│   │   │   ├── DatabaseHelper.java         # Gerenciador SQLite
│   │   │   ├── ClientAdapter.java          # Adaptador RecyclerView
│   │   │   ├── LicenseUtils.java           # Utilitários de licença
│   │   │   └── NotificationHelper.java     # Sistema de notificações
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_add_client.xml
│   │   │   │   ├── activity_client_details.xml
│   │   │   │   └── item_client.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   └── themes.xml
│   │   │   └── mipmap/
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── .github/
│   └── workflows/
│       └── build.yml                 # Pipeline CI/CD
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradlew
└── README.md
```

## 🗄️ Estrutura do Banco de Dados

### Tabela: `clients`

```sql
CREATE TABLE clients (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    app_type TEXT NOT NULL,
    mac TEXT NOT NULL,
    device_key TEXT NOT NULL,
    license_date TEXT NOT NULL,      -- Formato: dd/MM/yyyy
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

## 📖 Como Usar

### 1️⃣ Adicionar um Cliente
- Clique no botão **+** (FAB) na tela principal
- Preencha todos os campos obrigatórios
- Selecione a data de validade
- Clique em **Salvar**

### 2️⃣ Visualizar Detalhes
- Clique no card do cliente na lista
- Visualize todas as informações
- Veja o status atual da licença

### 3️⃣ Editar Cliente
- Clique no ícone **editar** no card
- Ou acesse detalhes e clique **Editar**
- Modifique os dados
- Clique em **Salvar**

### 4️⃣ Deletar Cliente
- Clique no ícone **deletar** no card
- Ou acesse detalhes e clique **Deletar**
- Confirme a exclusão

### 5️⃣ Atualizar Lista
- Clique em **Atualizar Lista** para recarregar dados
- A lista é atualizada automaticamente ao retornar da edição

## 🎨 Interface Visual

### Cores Utilizadas
- **Roxo (#6200EE)** - Cor primária principal
- **Verde (#4CAF50)** - Licença ativa
- **Laranja (#FF9800)** - Aviso de expiração
- **Vermelho (#F44336)** - Licença expirada

### Componentes Material Design
- CardView para cards de cliente
- FloatingActionButton para adicionar
- RecyclerView para lista responsiva
- DatePickerDialog para seleção de data

## 🔐 Segurança

- Dados armazenados localmente no SQLite
- Sem sincronização com servidores externos
- Validação de entrada em todos os campos
- Confirmação antes de deletar registros

## 📊 Exemplo de Dados

```
Nome: João Silva
App: Bob Player
MAC: 00:1A:2B:3C:4D:5E
Device Key: ABC123XYZ789
Data de Validade: 31/12/2025
Status: ✅ Licença Ativa (150 dias)
```

## 🤝 Contribuições

Contribuições são bem-vindas! Por favor:
1. Faça um Fork do projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

MIT License - Veja arquivo LICENSE para detalhes

## 👨‍💻 Desenvolvedor

Desenvolvido com ❤️ usando Android Studio

---

**Versão**: 1.0
**Última Atualização**: Setembro 2026
