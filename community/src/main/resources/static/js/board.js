async function getList({cno, size}){

    const result = await axios.get("/board/listing", { params: { cno: cno, size: size} });

    return result.data
}