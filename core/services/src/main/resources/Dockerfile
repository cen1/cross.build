FROM ubuntu:14.04
RUN apt-get update && \
apt-get install -y openssh-server && \
apt-get -y install default-jre && \
apt-get -y install git && \
mkdir /var/run/sshd && \
useradd -m crossbuild && echo "crossbuild:crossbuild" | chpasswd && adduser crossbuild sudo && \
mkdir /home/crossbuild/.ssh && \
touch /home/crossbuild/.ssh/authorized_keys && \
echo "PasswordAuthentication no" >> /etc/ssh/sshd_config && \
echo "crossbuild ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers && \
echo "ssh-rsa %PUBKEY% crossbuild_container" >> /home/crossbuild/.ssh/authorized_keys

# SSH login fix. Otherwise user is kicked off after login
RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd

ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]