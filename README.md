# git笔记

## git关联远程仓库

 - git init  创建本地仓库
 - git remote add origin wangqi@git.com 关联远程的仓库
 - git pull origin master 先将远程的文件拉取下来， 这时候会报一个history的错误，我们更改成git pull origin master --allow-unrelated-histories
 - 然后我们随便创建一个一个文件，add、commit
 - git push origin master提交到远程仓库

 
 ## git一些常用操作

 - git add .|<文件名> 将当前目录下所有的文件add到暂存区
 - git status  查看当前暂存区状态，红色表示还在工作区，未添加到暂存区，绿色表示已添加到暂存区
 - git diff 比较当前工作区与最近提交的对比
 - git commit -m "提交说明" 提交记录
 - git log 查看当前提交的所有记录
 - git reflog  如果在回退版本中，忘了commit的id，可以通过这个命令查看之前的操作
 - git reset HEAD^ | HEAD^1 回退上一个版本
 - git reset <commit id> 回退到指定版本的提交
 
 
## git的撤销和修改
 
此处分两种情况：
  
>当前的撤销操作在工作区，git status查看的时候是红色的
  - git checkout -- <文件名> 可以直接撤销当前的操作 
  
>当前的撤销操作在暂存区，git status查看的时候是绿色的
  - git reset HEAD a.txt 暂存区的修改撤销掉，重新放回工作区，这时候git status查看的时候是红色
  - git checkout -- <文件名> 和条件1一样，然后在进行撤销


> git的删除操作

  我们在提交的项目中，不小心删除了某个文件，我们可以通过git status查看，提示我们哪个文件被deleted掉了，这时候提示的是红色的，如果要恢复文件，我们可以使用
  - git checkout -- <被删除的文件> 就可以恢复删除的文件
  
  如果是想要真正删除某个文件，可以使用如下命令，git status查看的时候是绿色的
  - git rm <文件名> 
  
  git rm后，我们能否重新恢复文件呢？当然是可以的，git rm后，用git status查看当前是已经调教到暂存区了，我们是不是想到了撤销操作，是的，这时候，我们将暂存区的文件提到工作区，使用命令
  - git reset HEAD b.txt 这时候文件去到了工作区
  - git checkout -- b.txt 这时候文件被恢复了
  
## git分支操作

HEAD严格来说不是指向提交，而是指向master，master才是指向提交的，所以，HEAD指向的就是当前分支

![](https://cdn.liaoxuefeng.com/cdn/files/attachments/0013849087937492135fbf4bbd24dfcbc18349a8a59d36d000/0)

当我们创建新的分支，例如dev时，Git新建了一个指针叫dev，指向master相同的提交，再把HEAD指向dev，就表示当前分支在dev上

![](https://cdn.liaoxuefeng.com/cdn/files/attachments/001384908811773187a597e2d844eefb11f5cf5d56135ca000/0)

![](https://cdn.liaoxuefeng.com/cdn/files/attachments/0013849088235627813efe7649b4f008900e5365bb72323000/0)

Git创建一个分支很快，因为除了增加一个dev指针，改改HEAD的指向，工作区的文件都没有任何变化

### 分支操作

- 创建分支: git branch <分支名称>
- 切换分支：git checkout <分支名称>
- 创建并切换分支: git checkout -b <分支名称>
- 查看所有分支:git branch

### 合并分支操作

 -  git checkout -b dev 创建并切换到dev分支
 -  touch c.txt 创建文件c.txt，然后随便编辑一些内容
 -  git add c.txt 添加到暂存区
 -  git commit -m "c.txt" 提交
 -  git checkout master 切换到主分支
 -  git merge dev  将dev分支的内容合并到master分支去
 - git branch -d dev 删除dev分支

 
### 合并冲突
冲突造成的原因是，master分支对A文件进行了编辑和提交，feature1分支也对A文件进行了编辑和提交，这时候master要将feature1合并过来，这时候就会造成A文件合并冲突

如图

![](https://cdn.liaoxuefeng.com/cdn/files/attachments/001384909115478645b93e2b5ae4dc78da049a0d1704a41000/0)

-git merge feature1  进行合并报错

```
Auto-merging c.txt
CONFLICT (content): Merge conflict in c.txt
Automatic merge failed; fix conflicts and then commit the result.
```
- git status 可以查看冲突的是哪些文件

```
On branch master
You have unmerged paths.
  (fix conflicts and run "git commit")
  (use "git merge --abort" to abort the merge)

Unmerged paths:
  (use "git add <file>..." to mark resolution)

	both modified:   c.txt

no changes added to commit (use "git add" and/or "git commit -a")
```
我们open c.txt查看一下当前合并成啥了

```
dasdasdasd

<<<<<<< HEAD

我是主分支编辑了
=======
dev分支修改了
>>>>>>> dev
```
Git用<<<<<<<，=======，>>>>>>>标记出不同分支的内容，我们修改如下后保存

```
dasdasdasd
我是主分支编辑了
dev分支修改了
```
- git add  |   git commit 

提交后的分支变成了

![](https://cdn.liaoxuefeng.com/cdn/files/attachments/00138490913052149c4b2cd9702422aa387ac024943921b000/0)


使用如下命令，可以查看当前合并的状态
  - git log --graph --pretty=oneline --abbrev-commit
  - git log --graph 可以查看完整版
  
```
*   524c25f (HEAD -> master) hand merge
|\  
| * cbcca31 (dev) alert c
* | e5e0ad8 alsert c.txt
|/  
* a9b5b3a c
* 6ed2cb2 create b
* 133a271 第二次xiugai
* 6851475 第一次提交
```

最后删除分支
 - git branch -d 
 


### 分支分管理

合并分支时，如果可能，Git会用Fast forward模式，但这种模式下，删除分支后，会丢掉分支信息。

如果要强制禁用Fast forward模式，Git就会在merge时生成一个新的commit，这样，从分支历史上就可以看出分支信息

- git checkout -b dev 
- touch c.txt 随便加点内容
- git add | git commit 提交
- git checkout master 切换到主分支
- git merge --no-ff "merge no-ff dev" dev  将dev的分支以no-ff模式合并到master，并创建commit "merge no-ff dev"
- git log graph 查看一下
```
*   b63ae02 (HEAD -> master) merge dev no-ff
|\  
| * d0d0b78 (dev) dev-c.txt
|/  
*   524c25f hand merge
|\  
| * cbcca31 alert c
* | e5e0ad8 alsert c.txt
|/  
* a9b5b3a c
* 6ed2cb2 create b
* 133a271 第二次xiugai
* 6851475 第一次提交
```

![](https://cdn.liaoxuefeng.com/cdn/files/attachments/001384909222841acf964ec9e6a4629a35a7a30588281bb000/0)

合并分支时，加上--no-ff参数就可以用普通模式合并，合并后的历史有分支，能看出来曾经做过合并，而fast forward合并就看不出来曾经做过合并

其实很简单的一句话就是--no--ff模式合并的时候要加一个commit，可以根据这个commit看出当前是合并了


### 功能分支

对于一个dev分支，如果我们每增加一个功能，就去新建一个分支，然后合并到dev分支去，如果这时候公司说不要这个功能了，我们该怎么办呢？

操作
 - git checkout -b feature  创建并切换到feature分支
 - touch d.txt 创建一个文件
 - git add d.txt | git commit -m 
 - git checkout dev 切换到dev分支
 - 这时候新功能说不要了，我们只能删除这个功能分支，git branch -d feature ,删除失败，提示使用git branch -D feature
 
 ```
 error: The branch 'feature' is not fully merged.
If you are sure you want to delete it, run 'git branch -D feature'.
 ```
 - git branch -D feature 删除成功
 


## 远程分支操作

 - git remote 查看关联到远程库的信息
 - git remote rm origin 删除与远程仓库的关联
 - git remote -v 显示更详细的信息
 - git push origin master 提交到origin远程仓库master分支
 - git checkout -b dev origin/dev 创建远程origin的dev分支到本地
 
 ### 多人协作

- 首先，可以试图用git push origin <分支>推送自己的修改；

- 如果推送失败，则因为远程分支比你的本地更新，需要先用git pull试图合并；

- 如果合并有冲突，则解决冲突，并在本地提交；

- 没有冲突或者解决掉冲突后，再用git push origin <分支>推送就能成功！

- 如果git pull提示“no tracking information”，则说明本地分支和远程分支的链接关系没有创建，用命令git branch --set-upstream <分支> origin/<分支>



## 标签管理
 
发布一个版本时，我们通常先在版本库中打一个标签（tag），这样，就唯一确定了打标签时刻的版本。将来无论什么时候，取某个标签的版本，就是把那个打标签的时刻的历史版本取出来。所以，标签也是版本库的一个快照

Git的标签虽然是版本库的快照，但其实它就是指向某个commit的指针（跟分支很像对不对？但是分支可以移动，标签不能移动），所以，创建和删除标签都是瞬间完成的

### 打标签

```
appledeMacBook-Pro:versionBack apple$ git branch
  dev
* master
appledeMacBook-Pro:versionBack apple$ git tag v1.0
appledeMacBook-Pro:versionBack apple$ git tag
v1.0
appledeMacBook-Pro:versionBack apple$ git log --pretty=oneline --abbrev-commit
b63ae02 (HEAD -> master, tag: v1.0) merge dev no-ff
d0d0b78 (dev) dev-c.txt
524c25f hand merge
```

默认标签是打在最新提交的commit上的,如何要打到指定的commit上呢？使用git log查看当前所有的commit，然后复制需要打tag的commit id，然后使用命令git tag v1.0 <commit id>

```
appledeMacBook-Pro:versionBack apple$ git tag v1.1 524c25f
appledeMacBook-Pro:versionBack apple$ git log --pretty=oneline --abbrev-commit
b63ae02 (HEAD -> master, tag: v1.0) merge dev no-ff
d0d0b78 (dev) dev-c.txt
524c25f (tag: v1.1) hand merge
```

可以看到commit 记录后面打上了tag的标记

- git tag 可以查看所有的的tag
- git show <tagName> 可以显示指定tag的详细信息
- git tag -a v1.2 -m "这里可以写当前tag版本的说明" 3628164  创建带有说明的tag标签

```
appledeMacBook-Pro:versionBack apple$ git tag -a v1.2 -m "发布聊天版本" 133a271
appledeMacBook-Pro:versionBack apple$ git show v1.2
tag v1.2
Tagger: MRwangqi <794149142@qq.com>
Date:   Thu Feb 22 15:34:00 2018 +0800

发布聊天版本

commit 133a27176adcc351ee28f5898053be6cdb1517ee (tag: v1.2)
Author: MRwangqi <794149142@qq.com>
Date:   Thu Feb 22 11:01:50 2018 +0800
```
### 操作标签

- git tag -d v1.0  删除标签
- git push origin <tagName>  推送标签到远程
- git push origin --tags 将所有的标签推送到远程

#### 删除远程标签

- git tag -d v1.0 先删除本地的标签
- git push origin :refs/tags/v1.0 然后删除远程的标签



## 忽略特殊文件

 对于一些不想提交的文件，我们可以在项目文件下添加一个.gitignore文件，示例.gitignore文件[https://github.com/github/gitignore](https://github.com/github/gitignore),忽略的格式可以看该链接的文件格式
 
>忽略文件的原则是：

- 忽略操作系统自动生成的文件，比如缩略图等；
- 忽略编译生成的中间文件、可执行文件等，也就是如果一个文件是通过另一个文件自动生成的，那自动生成的文件就没必要放进版本库，比如Java编译产生的.class文件；
- 忽略你自己的带有敏感信息的配置文件，比如存放口令的配置文件。

如果发现想提交的文件提交不了，可以查看是不是被忽略掉了，如果想强制提交，可以使用
- git add -f <文件>

如果想检查当前的文件是被那句话给忽略掉了，可以使用
-git check-ignore -v <文件名> 
