def setup(Map config = [:]) {

    stage('Checkout') {
        echo "Checking out repo: ${config.repoUrl}, branch: ${config.branch ?: 'main'}"
        checkout([$class: 'GitSCM',
            branches: [[name: "*/${config.branch ?: 'main'}"]],
            userRemoteConfigs: [[url: "${config.repoUrl}"]]
        ])
    }

    stage('Install Dependencies') {
        echo "Installing Python dependencies..."
        sh 'pip install --upgrade pip'
        sh 'pip install -r requirements.txt'
    }

    stage('Code Compilation') {
        echo "Compiling Python code..."
        sh '''
            mkdir -p reports
            set -e
            # Compile all Python files, save output to report
            python -m py_compile $(find . -name "*.py") 2>&1 | tee reports/code_compile_report.txt
        '''
        archiveArtifacts artifacts: 'reports/code_compile_report.txt', followSymlinks: false
    }
}
