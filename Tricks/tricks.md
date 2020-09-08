## 遇到的一些杂类问题合计

### Vim搭建Python环境

- VIm和Python3

  如果用的是macOS，并且是系统自带的Vim，那么它默认是不支持Python3的，可以通过`vim --version | grep python`查看。

  那么首先就是要安装一个支持Python3的Vim，这里brew提供了支持，通过brew安装的Vim默认支持Python3，安装语句`brew install vim`。同时Mac自带的是Python2，所以还需要安装Python3，安装语句`brew install python3`。为了能让系统调用到我们后来通过brew安装的Vim和Python3，需要修改系统环境变量加载顺序:

  1. `sudo vim /etc/paths`
  2. 调整顺序，将`/usr/local/bin`调至第一顺位
  3. 重启终端
  4. 完成上述操作后即可用which命令查看调用的vim和python3来源。

- Vim插件

  推荐使用vim-plug进行Vim的插件管理，安装步骤如下：

  ```shell
  mkdir ~/.vim/autoload/
  cd ~/.vim/autoload/
  wget https://raw.githubusercontent.com/junegunn/vim-plug/master/plug.vim
  ```

  https://raw.githubusercontent.com的域名被DNS污染了，被解析成无效IP，需要挂代理。

  安装完成后只需要在.vimrc中将插件写在如下代码块中：

  ```sh
  call plug#begin('~/.vim/plugged')
  "插件写在这里"
  "Plug 'xxx/xxx' 格式像这样" 
  call plug#end()
  ```

  保存后执行`:PlugInstall`即可自动安装插件

- 关于jedi-vim插件无法联想第三方库文件的问题

  这个问题真是困扰了我一个下午，查找了很多资料，本来以为是macOS的问题，结果看到官方的一个[issue](https://github.com/davidhalter/jedi/issues/1188)上面提到jedi-vim还不支持Python3.7的第三方库查询，但是这个issue是两年前提出的并且作者说会在4-5个月内完成，不知道为什么还不支持：）。不过如果按照我上面的操作是不会遇到这个问题的，因为brew默认安装Python3.8，我之前用的是Python3.7。

  

