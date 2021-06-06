Near-Realtime Prediction with Flink
=========================================
This project stands as an end-to-end example of building a machine learning model (linear regression), exporting that 
model with jpmml and using that model for real time prediction with Flink streaming.


## Pre-Requisites

1. Docker
    + [Mac](https://download.docker.com/mac/stable/Docker.dmg)

1. [Gradle](https://gradle.org) - You have a few options here
    + If you're using Intellij, just make sure it's enabled.
    + Run `brew install gradle`

1. Kaggle Api Token (Not absolutely necessary but recommended)
    + See https://www.kaggle.com/docs/api#authentication


## Up & Running
First let's clone the repo and fire up our system,

```
git clone git@github.com:aedenj/flink-machine-learning-fish-market-example.git ~/projects/flink-ml-example
cd ~/projects/flink-ml-example;
```

### The Model

The linear regression model in the Jupyter notebook is built using the [Fish Market dataset on Kaggle](https://www.kaggle.com/aungpyaeap/fish-market)
If you'd like to step through the notebook yourself run,

```bash
cd ~/projects/flink-ml-example;
docker run --rm -p 8888:8888 -e JUPYTER_ENABLE_LAB=yes -e GRANT_SUDO=yes --user root -v ~/.kaggle:/home/jovyan/.kaggle -v "$PWD":/home/jovyan/work jupyter/pyspark-notebook
```

After the docker container is up and running you'll see output like this in the terminal

```
    To access the server, open this file in a browser:
        file:///home/jovyan/.local/share/jupyter/runtime/jpserver-16-open.html
    Or copy and paste one of these URLs:
        http://e2de1e96eae2:8888/lab?token=b9478a108f3b7b86b45f7131f10cb18a17bc337dbd45233d
        http://127.0.0.1:8888/lab?token=b9478a108f3b7b86b45f7131f10cb18a17bc337dbd45233d
```

Navigate to one of the urls listed in your terminal then open the file `model/model.ipynb`.

I find it helpful to have an alias for running Jupyter under docker. One possibility on unix systems is,

```bash
alias jupyterd='f(){ docker run --rm -p $1:8888 -e JUPYTER_ENABLE_LAB=yes -e GRANT_SUDO=yes --user root -v ~/.kaggle:/home/jovyan/.kaggle -v "$PWD":/home/jovyan/work jupyter/pyspark-notebook; unset -f f; }; f'
```

### The Job
