@Library('main-shared-library@abed/line-coverage-tool') _
def defaultValue = 'Value from Jenkins file'
def git_repo_name = scm.getUserRemoteConfigs()[0].getUrl().replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
def artifact_name = git_repo_name.replace('.', '-').toLowerCase()
def configuration = 'Release'
properties([
        parameters([
                string(name: 'BASE_VERSION', defaultValue: '4.0',description: 'The base version number for this artifact.'),
                string(name: 'SEALIGHTS_SERVER', defaultValue: 'https://prod-staging-gw.sealights.co/api'),
                string(name: 'SEALIGHTS_APPNAME', defaultValue: '${JOB_NAME}'),
                string(name: 'SEALIGHTS_CUSTOMER_ID', defaultValue: 'SeaLights'),
                string(name: 'ARTIFACT_NAME', defaultValue: 'line-level-coverage-tool'),
                booleanParam(name: 'RUN_INTEGRATION_MARK_SANE', defaultValue: true),
                string(name: 'MARK_SANE_FILE', defaultValue: './mark_sane.sh',description: 'Placeholder for java infra job to provide a script to be run after integration tests'),
                string(name: 'MARK_PENDING_FILE', defaultValue: './mark_pending.sh'),
                string(name: 'GITHUB_USERNAME', defaultValue: 'sldevopsd'),
                string(name: 'GITHUB_PASSWORD', defaultValue: ''),
                string(name: 'SET_VERSION_FILE', defaultValue: './sealights-maven-plugin-version.sh'),
                string(name: 'SL_SNYK_TARGET_REF', defaultValue: 'master_branch',description: 'Gregory\'s Snyk token is used'),
                string(name: 'SL_SNYK_TOKEN', defaultValue: 'sljavaagent',description: 'Gregory\'s Snyk token is used'),
                string(name: 'Infra_Version', defaultValue: '', description: 'infra version')
        ])
])
LineLevelCoverageTool([
        github_repo_name          :     'SL.OnPremise.LineCoverageTool',
        base_image_uri            :     '534369319675.dkr.ecr.us-west-2.amazonaws.com/sl-jenkins-java-agent:latest',
        ecr_uri                   :     '534369319675.dkr.ecr.us-west-2.amazonaws.com',
        BASE_VERSION              :     "${BASE_VERSION}",
        SEALIGHTS_SERVER          :     "${SEALIGHTS_SERVER}",
        SEALIGHTS_APPNAME         :     "${SEALIGHTS_APPNAME}",
        SEALIGHTS_CUSTOMER_ID     :     "${SEALIGHTS_CUSTOMER_ID}",
        ARTIFACT_NAME             :     "${ARTIFACT_NAME}",
        RUN_INTEGRATION_MARK_SANE :     "${RUN_INTEGRATION_MARK_SANE}",
        MARK_SANE_FILE            :     "${MARK_SANE_FILE}",
        MARK_PENDING_FILE         :     "${MARK_PENDING_FILE}",
        SET_VERSION_FILE          :     "${SET_VERSION_FILE}",
        SL_SNYK_TARGET_REF        :     "${SL_SNYK_TARGET_REF}",
        SL_SNYK_TOKEN             :     "${SL_SNYK_TOKEN}",
        Infra_Version             :     "${Infra_Version}",
        GITHUB_USERNAME           :     "${GITHUB_USERNAME}" ,
        GITHUB_PASSWORD           :     "${GITHUB_PASSWORD}"
])
