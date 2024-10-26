## Application 1: Rule Engine with AST (Server)

### Prerequisites
- Ensure you have [Docker](https://www.docker.com/get-started) and [Docker Compose](https://docs.docker.com/compose/) installed on your system.
- Confirm that the following ports are free:
    - **8080** (for the Rule Engine Backend Server)
    - **3306** (for the database connection)

### Setup Instructions

1. **Clone the Repository**
   Open your terminal and navigate to the directory where you want to clone the repository. Run the following command:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Verify Port Availability**
   Before proceeding, ensure that ports **8080** and **3306** are not in use. You can check for active connections using:
    - On **Linux/Mac**:
      ```bash
      lsof -i :8080
      lsof -i :3306
      ```
    - On **Windows**:
      ```bash
      netstat -ano | findstr :8080
      netstat -ano | findstr :3306
      ```

3. **Build and Run the Docker Image**
   Execute the following command to build and start the Docker containers. This process may take some time as it builds the necessary images:
   ```bash
   docker-compose up --build
   ```

4. **Access the Rule Engine Backend Server**
   Once the Docker containers are up and running, you can access the Rule Engine Backend Server at:
   ```
   http://localhost:8080
   ```

### Notes
- If you encounter any issues during the build process, check the terminal logs for errors and ensure that Docker has permission to access the necessary resources.
- For stopping the Docker containers, you can use `Ctrl + C` in the terminal or run:
  ```bash
  docker-compose down
  ```