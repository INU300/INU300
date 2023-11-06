async function getList({bno, page, size, goLast}){

    const result = await axios.get(`/api/reply/list/${bno}`, {params: {page, size}})

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))

        return getList({bno:bno, page:lastPage, size:size})

    }

    return result.data
}


async function addReply(replyObj) {
    const response = await axios.post(`/api/reply`,replyObj)
    return response.data
}

async function getReply(rno) {
    const response = await axios.get(`/api/reply/${rno}`)
    return response.data
}

async function modifyReply(replyObj) {

    const response = await axios.put(`/api/reply/type/${replyObj.rno}`, replyObj)
    return response.data
}

async function removeReply(rno) {
    const response = await axios.post(`/api/reply/type/${rno}`)
    return response.data
}

async function likeReply(rno, status) {
    const response = await axios.put(`/api/reply/${rno}/like?status=${status}`)
    return response.data
}

async function getReplyLike(rno, status) {
    const response = await axios.get(`/api/reply/${rno}/like?status=${status}`)
    return response
}