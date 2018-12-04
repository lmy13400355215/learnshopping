#### ----------20121203------------
## git基础配置
#### 1.配置用户名
##### git config --global user.name "你的用户名"
#### 2.配置邮箱
##### git config --global user.email "你的邮箱"
#### 3.编码配置
##### 避免git gui中的中文乱码
##### git config --global core.quotepath utf-8
##### 避免git status显示的中文文件名乱码
##### git config --global core.quotepath off
#### 4.其他
##### git config --global core.ignorecase false
## git ssh key pair 配置
#### 1.在git命令行窗口输入
##### ssh-keygen -t rsa -C "你的邮箱"
#### 2.然后一路回车，不要输入任何密码之类，生成ssh key pair
#### 3.在用户目录下生成.ssh文件夹，找到公钥和私钥 
##### id_rsa  id_rsa.pub 
#### 4.将公钥的内容复制 
#### 5.进入github网站，将公钥添加进去
#### settings--SSH and GPG keys--New SSH key--ADD SSH key
### 创建一个远程仓库
#### New repository--Create respository
### 初始化一个本地仓库
#### git init
### 将工作区文件添加到暂存区
#### git add hello.txt
### 将暂存区文件添加到本地仓库
#### git commit -m "内容描述"
### 关联本地仓库和远程仓库
#### git remote add origin 远程仓库地址
### 第一次推送：本地仓库提交到远程仓库
#### git push -u -f origin master
### 以后：本地仓库提交到远程仓库
#### git push origin master
###  -------------------------------------------
### 项目中文件上传到远程仓库
#### 新建一个远程仓库
#### 在/d/business目录下输入命令 
##### 克隆：git clone 远程仓库地址
#### 新建idea项目
#### File--settings--登录GitHub
#### 配置.gitignore文件
#### Terminal 输入命令
#### git init
#### git add .
#### git commit -m "first commit"
#### git remote add origin 远程仓库地址
#### 解除关联：(git remote remove origin)
#### git push -u origin master
### 后续提交：
#### git add .
#### git commit -m "..."
#### git push
### --------------------------------------------
### 企业项目开发模式 
#### 项目采用： 分支开发，主干发布  
#### 创建分支：git checkout -b v1.0 origin/master
#### 将分支推送到远程 git push origin HEAD -u 
#### 检查远程，发现多了v1.0分支 
### 项目提交到github 
#### .gitignore文件 :告诉Git哪些文件不需要添加到版本管理中 
#### 忽略规则：
#### 此为注释 – 将被 Git 忽略
##### *.a       # 忽略所有 .a 结尾的文件
##### !lib.a    # 但 lib.a 除外 /TODO     # 仅仅忽略项目根目录下的 TODO 文件，不包括 subdir/TODO build/    # 忽略 build/ 目录下的所有文件
##### doc/*.txt # 会忽略 doc/notes.txt 但不包括 doc/server/arch.txt
#### git add .  //提交所有
#### --------------------------------------------
### git验证
#### 执行git --version，出现版本信息，安装成功。 
#### --------------------------------------------
### git常用命令 
#### git init 创建本地仓库 
#### git add  添加到暂存区 
#### git commit -m "描述" 提交到本地仓库 
#### git remote add origin 远程仓库地址   关联本地仓库和远程仓库
#### git remote remove origin   解除关联
#### 本地仓库提交到远程仓库: git push -u origin master 
#### git status 检查工作区文件状态 
#### git log  查看提交committed 
#### git reset --hard committid  版本回退 
#### git branch 查看分支 
#### git checkout -b dev 创建并切换到dev分支 
#### 切换分支：git checkout 分支名 
#### 分支合并: git merge branchname
#### 将分支推送到远程 git push origin HEAD -u 
#### 更新远程代码到本地仓库: git fetch
#### 更新远程代码到本地仓库并合并分支: git pull
#### -----------20181204--------------
#### 
