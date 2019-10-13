# woldDiscortBot
----
Discord Bot Utils for Wolf Team channel administration

*13/08/2019*

El objetivo principal es refactorizar el código para obtener una aplicación Bot más modularizada fácil de entender, 
logrando con ello desarrollar nuevas características más fácilmente.

__TODO__:
- **[FIXED]** La función _update_ usa el hilo completo, lo cual hace que el bot no pueda contestar en ese tiempo (suele tomar 10 minutos)
- **[DONE]** Crear un servicio para leer properties y extraer lo que no encaje de la clase Constants.java a este nuevo fichero
- **[REMOVE] Usamos el de JDA** Log4j Está añadido pero no funciona, falta la configuración
- **[FIXED]** Main.java debería quedar prácticamente vacío. Habrá que crear capas de servicio para gestionar todas las funciones
- **[READY]** Una vez tengamos estas capas podremos empezar el desarrollo del bot para la gestión de muchísimas más funciones del clan
- **[IN_PROGRESS]** El Bot está pensado para actuar en cualquier canal del Discord del Clan, hay que ingeniar una capa que gestione los ID's 
de los canales/usuarios. Es decir, podrá ser usado por cualquier usuario pero habrá niveles de permisos que permitan realizar 
funciones u otras.
- **[DONE]** Gestión de permisos para las acciones/usuarios
- Cargar PBO's desde Discord
- etc

> NOTA: Todas estas tareas no son definitivas, quedan abierta a opiniones :D

- Algunas pequeñas tareas reportadas por __Nightmare__:
    - Gestionar calendario Google desde aqui con la API de Google 
       
- Algunas pequeñas tareas reportadas por __Itana__:
    - Mensaje de bienvenida
    - Hacer lo mismo que las convocatorias a las especialidades pero con las bandas y rangos.
    - Mensajes Recordatorios
    - Intentar que fuera de la tarjeta antes escriba el texto @wolfteam para que les salga a todos como notificaciones
    - @wolfteam no menciona
    - En la ayuda cambiar servidores `< nono > <TRGM> ------ CAMBIAR POR  < nono > Liberando Altis`
     
