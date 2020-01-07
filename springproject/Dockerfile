#from e o runtime da imagem
FROM openjdk:11.0.5-slim

#copy leva o codigo ou executavel para dentro do container | "." o ponto é a raiz do container
COPY target/movieapp.jar .

CMD ["java", "-jar", "movieapp.jar"]