# AIFavs Android 🎧✨

> An intelligent content management and podcast platform powered by AI

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

AIFavs is a modern Android application that helps users manage their favorite content collections, generate AI-powered podcasts, and interact with an intelligent assistant. The app combines content curation with artificial intelligence to provide a seamless multimedia experience.

## 📱 Features

### 🗂️ Smart Collections
- **URL-based Content Addition**: Simply paste any URL to automatically extract and organize content
- **AI-powered Categorization**: Automatic content categorization using machine learning
- **Tag Management**: Organize collections with custom tags for easy discovery
- **Smart Summaries**: AI-generated summaries and highlights for quick content overview

### 🎧 AI Podcast Generation
- **Text-to-Audio Conversion**: Transform your collected articles and content into engaging podcasts
- **Multiple Status Tracking**: Real-time status updates (Generating → Ready → Error)
- **Offline Playback**: Download and listen to generated podcasts offline
- **Background Playback**: Continue listening while using other apps

### 🤖 Intelligent Assistant
- **Chat Interface**: Natural language conversations with AI assistant
- **Streaming Responses**: Real-time response streaming for better user experience
- **Context Awareness**: Assistant understands your content collections and preferences
- **Content Recommendations**: Get personalized suggestions based on your interests

### 📊 Insights & Analytics
- **Usage Statistics**: Track your reading and listening habits
- **Content Analysis**: Detailed insights into your collection patterns
- **Trend Discovery**: Identify popular topics and content types

### 🎵 Advanced Media Player
- **Modern UI**: Material Design 3 compliant interface
- **Media Controls**: Play, pause, seek, forward/backward (10s/30s)
- **Mini Player**: Persistent player controls across the app
- **Background Service**: Continuous playback with notification controls
- **Media Session**: Integration with Android Auto and system media controls

## 🏗️ Architecture

This project follows **Modern Android Development** practices:

- **MVVM Architecture**: Clear separation of concerns with ViewModels and LiveData
- **Repository Pattern**: Centralized data management and API abstraction
- **Dependency Injection**: Efficient resource management
- **Single Activity Architecture**: Navigation Component with fragment-based navigation
- **Reactive Programming**: RxJava for asynchronous operations
- **Modern UI**: Material Design 3 with View Binding

### Tech Stack

#### Core
- **Kotlin** - Primary programming language
- **Android Jetpack** - Navigation, Lifecycle, LiveData, ViewModel
- **Material Design 3** - Modern UI components and theming

#### Networking
- **Retrofit** - REST API client
- **OkHttp** - HTTP client with SSE (Server-Sent Events) support
- **Gson** - JSON serialization/deserialization
- **Kotlin Serialization** - Type-safe serialization

#### Media & Playback
- **Media3 ExoPlayer** - High-performance media playback
- **Media3 Session** - Advanced media session management
- **Foreground Service** - Background audio playback

#### UI & UX
- **View Binding** - Type-safe view references
- **Glide** - Image loading and caching
- **SwipeRefreshLayout** - Pull-to-refresh functionality
- **BaseRecyclerViewAdapterHelper** - Enhanced RecyclerView adapters

#### Reactive Programming
- **RxJava/RxAndroid** - Asynchronous and event-based programming
- **RxKotlin** - Kotlin-specific RxJava bindings

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK API 24 (Android 7.0) or higher
- JDK 8 or higher
- Gradle 7.0+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/ai-favs-android.git
   cd ai-favs-android
   ```

2. **Configure API endpoint**
   
   Create a `local.properties` file in the root directory:
   ```properties
   base_url=https://your-api-endpoint.com
   ```

3. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

4. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```

### Configuration

The app uses a configuration-based approach for different environments:

- **Debug Build**: Uses `base_url` from `local.properties`
- **Release Build**: Uses `base_url` from `gradle.properties`

## 📁 Project Structure

```
app/src/main/java/com/example/aifavs/
├── assistant/           # AI Chat functionality
├── base/               # Base classes for activities
├── collections/        # Content collection management
├── extensions/         # Kotlin extensions
├── insights/          # Analytics and insights
├── playback/          # Media player and audio services
│   └── player/        # Player implementation
├── podcast/           # Podcast generation and management
├── settings/          # App settings and preferences
├── MainActivity.kt    # Main app entry point
├── Models.kt         # Data models and DTOs
└── RemoteApi.kt      # API interface definitions
```

## 🔧 Build Variants

The project supports multiple build configurations:

- **Debug** (`debug`): Development build with debugging enabled
- **Release** (`release`): Production build with code optimization

### Version Management

Version information is managed through `gradle.properties`:
```properties
major=0
minor=1  
patch=0
```

Generates version name: `0.1.0` and version code: `100`

## 🤝 Contributing

We welcome contributions from the community! Please follow these guidelines:

### Development Workflow

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful commit messages
- Add appropriate comments for complex logic
- Ensure all tests pass before submitting PR

### Reporting Issues

- Use the [GitHub Issues](https://github.com/your-username/ai-favs-android/issues) page
- Provide detailed reproduction steps
- Include device information and Android version
- Attach relevant logs when possible

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Material Design](https://material.io/) for design guidelines
- [ExoPlayer](https://exoplayer.dev/) for media playback capabilities
- [Retrofit](https://square.github.io/retrofit/) for networking
- [RxJava](https://github.com/ReactiveX/RxJava) for reactive programming

## 📞 Support

For support and questions:

- 📧 Email: [your-email@example.com](mailto:your-email@example.com)
- 💬 Issues: [GitHub Issues](https://github.com/your-username/ai-favs-android/issues)
- 📖 Wiki: [Project Wiki](https://github.com/your-username/ai-favs-android/wiki)

---

**Made with ❤️ by the AIFavs Team** 