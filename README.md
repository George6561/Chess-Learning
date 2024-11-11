Chess-Engine

Overview
Chess-Engine is a fully-fledged chess application powered by the Stockfish engine and Monte Carlo simulations for evaluating and analyzing chess positions. Developed as part of a university capstone project, this engine aims to provide both strategic depth and robust move analysis, suitable for research and gameplay.

Features
Stockfish Integration: Utilizes the Stockfish engine to analyze and make optimal moves.
Monte Carlo Simulations: Performs simulations to assess positions and validate moves.
JavaFX User Interface: Interactive chessboard interface for users to play against the engine.
Move Evaluation: Real-time move evaluations are displayed after each move.
Javadoc Documentation: Comprehensive documentation for all classes and methods for easy navigation.
GitHub Pages Integration: Javadocs hosted online for ease of access.
Project Structure
plaintext
Copy code
Chess-Stockfish-Engine/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── chess/
│   │   │   │   ├── montecarlo/
│   │   │   │   ├── board/
│   │   │   │   ├── stockfish/
│   │   │   │   └── window/
│   │   │   └── module-info.java
│   │   └── resources/
│   │       └── images/
│   ├── test/  (future test files)
├── docs/  (Javadocs)
├── target/
│   └── ... (build outputs)
└── README.md
Installation and Setup
Prerequisites
Java 17 or higher
NetBeans IDE or any Java-compatible IDE
Maven
Clone the Repository
bash
Copy code
git clone https://github.com/George6561/Chess-Engine.git
cd Chess-Engine
Building the Project
Use Maven to build the project:

bash
Copy code
mvn clean install
Running the Application
Run the main class com.chess.main.Main through your IDE or use Maven:

bash
Copy code
mvn exec:java -Dexec.mainClass="com.chess.main.Main"
Usage
Launch the application to open the JavaFX chessboard interface.
Play as White against the Stockfish-powered AI.
View real-time move evaluations for better strategic insights.
Javadoc Documentation
Detailed Javadocs are available here (replace with your actual GitHub Pages link if applicable).

Contributing
Contributions are welcome! Whether it's fixing bugs, improving documentation, or suggesting new features, feel free to fork the project and submit a pull request.

License
This project is licensed under the MIT License.

Author
George Miller
University Capstone Project, 2024
Contact: LinkedIn Profile (replace with your actual profile if needed)

Acknowledgements
Stockfish Engine: Stockfish Developers
JavaFX: OpenJFX
Future Enhancements
Multiplayer mode for playing against friends.
Integration with online chess databases for move validation.
Improved AI with deeper Monte Carlo simulations.
Support for custom game analysis and report generation.

