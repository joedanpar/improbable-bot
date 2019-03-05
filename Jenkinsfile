#!groovy
/*******************************************************************************
 * This file is part of Improbable Bot.
 *
 *     Improbable Bot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Improbable Bot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Improbable Bot.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                bat 'gradlew.bat build'
            }
        }

        stage('Sonar') {
            steps {
                withCredentials([usernamePassword(credentialsId: '51fc6c9c-7764-40e5-af76-e88a00b57aad', passwordVariable: 'token', usernameVariable: 'username')]) {
                    bat "gradlew.bat sonarqube -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=${token} -Dsonar.organization=${username}-github -Dsonar.projectKey=${username}_improbable-bot -Dsonar.branch.name=${env.BRANCH_NAME}"
                }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', onlyIfSuccessful: true
            }
        }

        stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                build job: 'UpdateImprobabot', wait: false
            }
        }
    }
}
