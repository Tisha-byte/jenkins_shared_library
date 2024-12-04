def call(Map config) {
   
    
    def docker_hub_cred = config.docker_hub_cred ?: "No credentials provided"
    def image = config.image
    def buildnum = config.buildnum

    
    def docker_image=docker.build("${image}:${buildnum}", ".")

    
    docker.withRegistry('https://index.docker.io/v1/', config.docker_hub_cred) {
       docker_image.push()
    }
}
