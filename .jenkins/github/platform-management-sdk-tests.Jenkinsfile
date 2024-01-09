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
            steps {
                echo "Running from branch ${env.ghprbSourceBranch} on ${testInstanceDomain}"
                script {
                    if (['10.18.0.x', '10.17.0.x', '10.16.0.x', '10.15.0.x']
                        .any {env.ghprbSourceBranch.startsWith(it)}) {
                        // for versions 10.18 and before sdk is a part of cumulocity component
                        systemVersion = GetVersionFromSourceBranch('cumulocity')
                    } else if (env.ghprbSourceBranch.contains('java-sdk')) {
                        // when run for java-sdk component, take the version from its descriptor
                        systemVersion = GetVersionFromSourceBranch('java-sdk')
                    } else {
                        // otherwise runs for cumulocity component and is not yet independently
                        // released, so keep taking the version of cumulocity component
                        systemVersion = GetVersionFromSourceBranch('cumulocity')
                    }
                }
                echo "Running tests for platform version ${systemVersion} on ${testInstanceDomain}"
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

                echo "Checkout version ${systemVersion} of tests"
                checkout(scm: [
                    $class: 'GitSCM',
                    branches: [[name: "refs/tags/clients-java-${systemVersion}"]],
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
