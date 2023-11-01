FROM ubuntu:20.04

# update apt
RUN apt-get update -y && apt-get upgrade -y

# install pyenv
RUN apt-get install -y git curl wget make build-essential libssl-dev zlib1g-dev libbz2-dev libreadline-dev libsqlite3-dev libffi-dev

# install pyenv
ENV PYENV_ROOT /pyenv
ENV PATH $PYENV_ROOT/shims:$PYENV_ROOT/bin:$PATH
RUN git clone https://github.com/pyenv/pyenv.git $PYENV_ROOT && \
    cd $PYENV_ROOT && \
    git checkout $(git describe --abbrev=0 --tags) && \
    eval "$(pyenv init -)" && \
    pyenv install 3.12.0 && \
    pyenv global 3.12.0

# install poetry
RUN curl -sSL https://install.python-poetry.org | python -
ENV PATH /root/.local/bin:$PATH

# Set the timezone
ENV TZ=Asia/Tokyo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# install openjdk-17
RUN apt-get install -y openjdk-17-jdk

RUN apt-get install -y smem --fix-missing

RUN apt-get install -y bc

COPY . /app

RUN chmod +x /app/benchmark.sh

WORKDIR /app
CMD [ "./benchmark.sh" ]

