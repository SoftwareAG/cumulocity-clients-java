@Library('c8y-common-steps') _
def INSTANCE_NAME
def ADMIN_CREDENTIALS_ID
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
                    ADMIN_CREDENTIALS_ID = "post-merge-admin"
                    INSTANCE_NAME = postMergeTestingDomain(env.ghprbSourceBranch)
                }
            }
        }

        stage('Initialize for staging') {
            when {
                expression { return "${env.ghprbSourceBranch}".contains('/Staging') }
            }
            steps{
                script {
                    ADMIN_CREDENTIALS_ID = "e2eAdmin"
                    INSTANCE_NAME = stagingTestingDomain(env.ghprbSourceBranch)
                }
            }
        }

        stage('Get platform version') {
            environment {
                ADMIN_CREDENTIALS = credentials("${ADMIN_CREDENTIALS_ID}")
            }
            steps {
                echo "Running from branch ${env.ghprbSourceBranch} on ${INSTANCE_NAME}"
                sh 'curl -k -u ${ADMIN_CREDENTIALS} http://management.'+ INSTANCE_NAME + ':8111/tenant/system/options/system/version > system-version.json'
                script {
                    def systemVersion = readJSON file: 'system-version.json'
                    env.SYSTEM_VERSION = systemVersion.value
                }
                echo "Running tests for platform version ${env.SYSTEM_VERSION} on ${INSTANCE_NAME}"
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
                              -Pintegration -Dcumulocity.host=http://${INSTANCE_NAME}:8111 \
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
                                string(name: "DOMAIN", value: "${INSTANCE_NAME}"),
                                string(name: "TENANT_PREFIX", value: "plama-sdk"),
                                string(name: "TEST_MODE", value: "false")
                        ],
                        propagate: false, wait: true
                }
            }
        }
    }
}