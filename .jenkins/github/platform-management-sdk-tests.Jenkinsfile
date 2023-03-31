def INSTANCE_NAME
def ADMIN_CREDENTIALS_ID
pipeline {
    agent {
        kubernetes {
            yaml '''
        apiVersion: v1
        kind: Pod
        spec:
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
            stages {
                stage('Set Post-Merge credential ID') {
                    steps{
                        script {
                            ADMIN_CREDENTIALS_ID = "post-merge-admin"
                        }
                    }
                }
                stage('Instance name until 10.16 ') {
                    when {
                        expression { return env.ghprbSourceBranch =~ /10.1[3456].0.x/ }
                    }
                    steps {
                        script {
                            INSTANCE_NAME = "pm-${env.ghprbSourceBranch.split('/')[0].replaceAll('\\.','-')}"
                        }
                        echo "Running from branch ${env.ghprbSourceBranch} on ${INSTANCE_NAME}.stage.c8y.io"
                    }
                }
                stage('Instance name since 10.17') {
                    when {
                        expression { return !(env.ghprbSourceBranch =~ /10.1[3456].0.x/) }
                    }
                    steps {
                        script {
                            INSTANCE_NAME = "pm-${env.ghprbSourceBranch.split('/')[0].replaceAll('\\.','').replaceAll('0x','')}-cumulo"
                        }
                        echo "Running from branch ${env.ghprbSourceBranch} on ${INSTANCE_NAME}-cumulo.stage.c8y.io"
                    }
                }
            }
        }

        stage('Initialize for staging') {
            when {
                expression { return "${env.ghprbSourceBranch}".contains('/Staging') }
            }
            stages {
                stage('Set Staging credential ID') {
                    steps{
                        script {
                            ADMIN_CREDENTIALS_ID = "e2eAdmin"
                        }
                    }
                }
                stage('Instance name until 10.17 ') {
                    when {
                        expression { return env.ghprbSourceBranch =~ /10.1[34567].0.x/ }
                    }
                    steps {
                        script {
                            OLD_INSTANCES = [
                                    "10.13.0.x" : "4",
                                    "10.14.0.x" : "3",
                                    "10.15.0.x" : "2",
                                    "10.16.0.x" : "1",
                                    "10.17.0.x" : "basic"
                            ]
                            INSTANCE_NAME = OLD_INSTANCES[env.ghprbSourceBranch.split('/')[0]]
                        }
                        echo "Running from branch ${env.ghprbSourceBranch} on ${INSTANCE_NAME}.stage.c8y.io"
                    }
                }
                stage('Instance name since 10.18') {
                    when {
                        expression { return !(env.ghprbSourceBranch =~ /10.1[34567].0.x/) }
                    }
                    steps {
                        script {
                            INSTANCE_NAME = "stg-${env.ghprbSourceBranch.split('/')[0].replaceAll('\\.','-')}"
                        }
                        echo "Running from branch ${env.ghprbSourceBranch} on ${INSTANCE_NAME}.stage.c8y.io"
                    }
                }
            }
        }

        stage('Get platform version') {
            environment {
                ADMIN_CREDENTIALS = credentials("${ADMIN_CREDENTIALS_ID}")
            }
            steps {
                sh 'curl -k -u ${ADMIN_CREDENTIALS} http://management.'+ INSTANCE_NAME + '.stage.c8y.io:8111/tenant/system/options/system/version > system-version.json'
                script {
                    def systemVersion = readJSON file: 'system-version.json'
                    env.SYSTEM_VERSION = systemVersion.value
                }
                echo "Running tests for platform version ${env.SYSTEM_VERSION} on ${INSTANCE_NAME}.stage.c8y.io"
            }
        }

        stage('Checkout clients-java repository') {
            steps {
                checkout([
                        $class: 'GitSCM',
                        branches: [[name: "refs/tags/clients-java-${env.SYSTEM_VERSION}"]],
                        userRemoteConfigs: scm.userRemoteConfigs
                ])
            }
        }

        stage('Execute SDK tests') {
            environment {
                ADMIN_CREDENTIALS = credentials("${ADMIN_CREDENTIALS_ID}")
                MVN_SETTINGS = credentials("maven-settings")
            }
            steps {
                container('java') {
                    script {
                        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                            sh """
                            ./mvnw -B -s $MVN_SETTINGS verify \
                              -Pintegration -Dcumulocity.host=http://${INSTANCE_NAME}.stage.c8y.io:8111 \
                              -Dcumulocity.management.password=$ADMIN_CREDENTIALS_PSW \
                              -Dcumulocity.management.username=$ADMIN_CREDENTIALS_USR
                            """
                        }
                    }
                }
            }
            post {
                always {
                    junit(testResults: '**/TEST-*.xml', keepLongStdio: true, allowEmptyResults: true)
                    archiveArtifacts(artifacts: '**/TEST-*.xml', allowEmptyArchive: true)
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
                                string(name: "DOMAIN", value: "${INSTANCE_NAME}.stage.c8y.io"),
                                string(name: "TENANT_PREFIX", value: "plama-sdk"),
                                string(name: "TEST_MODE", value: "false")
                        ],
                        propagate: false, wait: true
                }
            }
        }
    }
}