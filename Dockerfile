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

# # install poetry
# RUN curl -sSL https://raw.githubusercontent.com/python-poetry/poetry/master/get-poetry.py | python -
# ENV PATH /root/.poetry/bin:$PATH



