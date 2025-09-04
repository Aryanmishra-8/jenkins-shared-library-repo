def install(String version) {
    sh """
    if ! ./gitleaks version > /dev/null 2>&1; then
        echo " Installing Gitleaks v${version}..."
        curl -sSL https://github.com/gitleaks/gitleaks/releases/download/v${version}/gitleaks_${version}_linux_x64.tar.gz -o gitleaks.tar.gz
        tar -xzf gitleaks.tar.gz
        chmod +x gitleaks
        rm -f gitleaks.tar.gz
    else
        echo " Gitleaks already installed."
    fi
    """
}

def scan(String reportFile) {
    sh "./gitleaks detect --source . --report-format html --report-path ${reportFile}"
}
