mdc.autoInit()
function fetchPath (e, path, method) {
    e.preventDefault()
    fetch(path, {method})
}