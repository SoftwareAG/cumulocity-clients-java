@Library('c8y-common-steps') _

def adminCredentialsId
def testInstanceDomain
def systemVersion

pipeline {
    agent {
        kubernetes {
            yaml '''
        apiVersion: v1
        kind: Pod
        spec:
          imagePullSecrets:
          - name: "ci-registry-pull"
          containers:
          - name: "java"
            image: "registry.stage.c8y.io/ci/jdk:11"
            command: ["/bin/sh"]
            args: ["-c", "cat"]
            tty: true
            env:
            - name: JENKINS_URL
              value: http://jenkins.ci.svc.cluster.local:8080/
            - name: MAVEN_USER_HOME
              value: /home/jenkins/agent
            - name: DOCKER_HOST
              value: tcp://localhost:2375
            workingDir: "/home/jenkins/agent"
            resources:
              requests:
                cpu: 2
                memory: 3Gi
              limits:
                cpu: 3
                memory: 3Gi
        '''
        }
    }
    options {
        timeout(time: 8, unit: 'HOURS')
        skipDefaultCheckout()
    }
    stages {
        stage('Initialize for post-merge') {
            when {
                expression { return env.ghprbSourceBranch.contains('/PostMerge') }
            }
            steps{
                script {
                    adminCredentialsId = "post-merge-admin"
                    testInstanceDomain = postMergeTestingDomain(env.ghprbSourceBranch)
                }
            }
        }

        stage('Initialize for staging') {
            when {
                expression { return env.ghprbSourceBranch.contains('/Staging') }
            }
            steps{
                script {
                    adminCredentialsId = "e2eAdmin"
                    testInstanceDomain = stagingTestingDomain(env.ghprbSourceBranch)
                }
            }
        }

        stage('Get platform version') {
            environment {
                ADMIN_CREDENTIALS = credentials("${adminCredentialsId}")
            }
            steps {
                echo "Running from branch ${env.ghprbSourceBranch} on ${testInstanceDomain}"
                sh 'curl -k -u ${ADMIN_CREDENTIALS} http://management.'+ testInstanceDomain + ':8111/tenant/system/options/system/version > system-version.json'
                script {
                    systemVersion = readJSON file: 'system-version.json'
                }
                echo "Running tests for platform version ${systemVersion.value} on ${testInstanceDomain}"
            }
        }

        stage('Checkout') {
            steps {
                echo 'Checkout current version of jenkins scripts'
                checkout(scm: [
                    $class: 'GitSCM',
                    branches: scm.branches,
                    userRemoteConfigs: scm.userRemoteConfigs,
                    extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths: [[path: '.jenkins/*']]]]
                ])
                sh 'mv ${WORKSPACE}/.jenkins ${WORKSPACE_TMP}/'

                echo "Checkout version ${systemVersion.value} of tests"
                checkout(scm: [
                    $class: 'GitSCM',
                    branches: [[name: "refs/tags/clients-java-${systemVersion.value}"]],
                    userRemoteConfigs: scm.userRemoteConfigs,
                    extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths: [[path: '/*'], [path: '!.jenkins']]]]
                ])
                sh 'mv ${WORKSPACE_TMP}/.jenkins ${WORKSPACE}/'
            }
        }

        stage('Run tests') {
            environment {
                MVN_SETTINGS = credentials('maven-settings')
                MAVEN_PROFILES = 'integration'
                ADMIN_CREDENTIALS = credentials("${adminCredentialsId}")
            }
            steps {
                container('java') {
                    script {
                        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                            sh """\
                                .jenkins/scripts/mvn.sh verify \\
                                    --file java-client \\
                                    --define 'cumulocity.host=http://${testInstanceDomain}:8111'
                               """
                        }
                    }
                }
            }
            post {
                always {
                    junit(testResults: '**/TEST-*.xml', keepLongStdio: true, allowEmptyResults: true)
                }
                success {
                    createSummary(icon: 'green.gif', text: 'SDK tests passed')
                }
                failure {
                    createSummary(icon: 'error.gif', text: 'SDK tests failed')
                }
            }
        }
    }
    post {
        always {
            script {
                if (env.ghprbSourceBranch.contains('/Staging')) {
                    build job: "cucumber-tenants-cleanup",
                        parameters: [
                                string(name: "DOMAIN", value: "${testInstanceDomain}"),
                                string(name: "TENANT_PREFIX", value: "plama-sdk"),
                                string(name: "TEST_MODE", value: "false")
                        ],
                        propagate: false, wait: true
                }
            }
        }
    }
}
