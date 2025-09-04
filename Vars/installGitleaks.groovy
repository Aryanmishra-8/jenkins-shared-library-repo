def call() {
    sh '''
    if ! ./gitleaks version > /dev/null 2>&1; then
        curl -sSL https://github.com/gitleaks/gitleaks/releases/download/v8.18.2/gitleaks_8.18.2_linux_x64.tar.gz -o gitleaks.tar.gz
        tar -xzf gitleaks.tar.gz
        chmod +x gitleaks
        rm -f gitleaks.tar.gz
    fi
    '''
}
