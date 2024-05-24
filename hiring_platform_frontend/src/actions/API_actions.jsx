import axios from "axios"

const API_BASE_URL = 'http://192.168.1.20:8081/api/';


const api = axios.create({
  baseURL: API_BASE_URL,
});



const accessToken = localStorage.getItem('token');
if (accessToken) {
    api.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
}



// interceptors

api.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem('token');
        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);




// Api calls

export const contestApi = async () => {
  try {
    const response = await api.get("v1/contest?required=CONTEST");
    return response.data.object
  }
  catch (err) {
    console.log("Error in fetching Contest" + err)
  }
};

export const singleContestApi = async (id) => {
  // console.log(id)
  try {
   
    const response = await api.get(`v1/contest/${id}/result`, {
      params: {
        finalResult: "false"
      }
    });
    console.log(response.data.object);
    return response?.data?.object
  }
  catch (err) {
    console.log("Error in fetching Particular Contest" + err)
  }
};

export const fetchRoundApi = async (contestId,roundId) => {
  try {
    const response = await api.get(`v1/contest/${contestId}/result`, {
      params: {
        finalResult: "false",
        roundId:roundId
      }
    });
    // console.log(response.data.object);
    return response?.data?.object
  }
  catch (err) {
    console.log("Error in fetching Contest Round" + err)
  }
};

export const fetchFinalRoundApi = async (contestId,roundId,) => {
  try {
    const response = await api.get(`v1/contest/${contestId}/result`, {
      params: {
        finalResult: "true",
        roundId:roundId
      }
    });
    // console.log(response.data);
    return response?.data
  }
  catch (err) {
    console.log("Error in fetching Contest Final Round" + err)
  }
};

export const partcipantsRoundApi = async (contestId) => {
  try {
    const response = await api.get(`v1/contest/${contestId}?required=CONTESTANTS`);
    // console.log(response.data);
    return response?.data?.object
  }
  catch (err) {
    console.log("Error in fetching partcipants" + err)
  }
};

export const xlContestApi =  async(id) => {
  // console.log(id)
  try {
    const res = await api.get(`v1/final/${id}`,{
      responseType : 'blob',
    });
      const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      const url = window.URL.createObjectURL(blob);

      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'filename.xlsx');
      document.body.appendChild(link);
      link.click();
  
      URL.revokeObjectURL(url);
      document.body.removeChild(link);
  }
  catch (err) {
    console.log("Error in downloading xl file" + err)
  }
};

export const finalResultApi = async (contestId,userId) => {
  try {
    const response = await api.get(`v1/result/${contestId}/${userId}`);
    // console.log(response?.data?.object)
    return response?.data?.object
  }
  catch (err) {
    console.log("Error in final result" + err)
  }
};