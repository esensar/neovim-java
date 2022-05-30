" Initialize the channel
if !exists('s:neovimJavaJobId')
  let s:neovimJavaJobId = 0
endif

" Path to JAR
let s:bin = expand('<sfile>:p:h').'/../target/rplugin-example.jar'

" Entry point. Initialize RPC
function! s:connect()
  let id = s:initRpc()

  if 0 == id
    echoerr "neovim-java-rplugin-example: cannot start rpc process"
  elseif -1 == id
    echoerr "neovim-java-rplugin-example: rpc process is not executable"
  else
    " Mutate our jobId variable to hold the channel ID
    let s:neovimJavaJobId = id
  endif
endfunction

" Prints logs from java process error stream
function! Receive(job_id, data, event)
  echom printf('%s: %s',a:event,string(a:data))
endfunction


" Initialize RPC
function! s:initRpc()
  if s:neovimJavaJobId == 0
    let jobid = jobstart(['java', '-jar', s:bin], { 'rpc': v:true, 'on_stderr': 'Receive' })
    return jobid
  else
    return s:neovimJavaJobId
  endif
endfunction

call s:connect()
